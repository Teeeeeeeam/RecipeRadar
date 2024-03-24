package com.team.RecipeRadar.crawling.util;

import com.team.RecipeRadar.crawling.dto.RecipeDto;
import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Component
public class RecipePostScrapers {

    private String postNumber;

    private String imageUrl;

    private String title;

    private String content;

    private String serving;

    private String cookingTime;

    private String cookingLevel;

    private LinkedHashMap<String, LinkedHashMap<String, String>> ingredientMap = new LinkedHashMap<>();
    private LinkedHashMap<String, String> cookingSteps = new LinkedHashMap<>();


    CrawlingUtil urlBuilder = new CrawlingUtil();

    List<RecipeDto> recipeDtoList = new ArrayList<>();

    private void processRecipeUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Element mainThumbsImage = document.getElementById("main_thumbs");
            imageUrl = mainThumbsImage.attr("src");

            log.info("imageUrl={}", imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<RecipeDto> repeatedWorkScrap() throws UrlBuildExceptionHandler {

        final int maxPages =10;

        int count = 0;

        List<String> rankList1;

        List<String> recipeRankList = new CrawlingUtil().getRecipeRankList();
        List<RecipeDto> allRecipeDtoList = new ArrayList<>();




       for (String recipe : recipeRankList) {

            List<RecipeDto> recipeDto = postScrap(recipe, maxPages);
            allRecipeDtoList.addAll(recipeDto);
           // 5초 쉬기
           try {

           TimeUnit.SECONDS.sleep(5);
           }catch (InterruptedException  e){
               e.printStackTrace();
           }
        }

            return allRecipeDtoList;
    }



    public List<RecipeDto> postScrap(String recipe, int maxPages) throws UrlBuildExceptionHandler {
        // 현재 자원 코어수 * 2 스레드풀 생성
        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(2 * coreCount);

        List<String> recipePostUrls = urlBuilder.searchPagedRecipeUrlBuilder(recipe, maxPages);

        List<CompletableFuture<Void>> futures = recipePostUrls.stream()
                .map(url -> CompletableFuture.runAsync(() -> {
                    boolean success = false;
                    int retryCount = 0;
                    while (!success && retryCount < 3) { // 최대 3번까지 재시도
                    try {

                        Connection connection = Jsoup.connect(url);

                        Document document = connection.get();

                        RecipeDto recipeDto = extractRecipeInfo(document);

                        recipeDtoList.add(recipeDto);


                        success = true; // 성공했을 때 플래그 설정
                    } catch (IOException e) {
                        log.error("URL 레시피 데이터 가져오는도중 ERROR: {}", url, e);
                        retryCount++; // 재시도 횟수 증가
                        if (retryCount < 3) {
                            // 재시도 간격 설정 (예: 5초)
                            try {
                                TimeUnit.SECONDS.sleep(5);
                            } catch (InterruptedException interruptedException) {
                                log.error("Interrupted while waiting for retry", interruptedException);
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                    }
                }, executorService))
                .collect(Collectors.toList());

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 모든 작업이 완료되면 스레드 풀 종료 후 recipeDtoList 반환
        allOf.join();
        executorService.shutdown();

        return recipeDtoList;
    }

    private RecipeDto extractRecipeInfo(Document document) {

        //이미지
        Element mainThumbsImage = document.getElementById("main_thumbs");
        if (mainThumbsImage == null) {
            log.info("메인 이미지 없는 게시물 입니다.");
        }else {
            imageUrl = mainThumbsImage.attr("src");
            //log.info("imageUrl={}", imageUrl);
        }
        //제목
        Elements elements = document.getElementsByClass("view2_summary st3");
        title = elements.select("h3").text();

        //내용
        Elements contentElements = document.getElementsByClass("view2_summary_in");
        content = contentElements.html();

        //게시글 번호
        Elements postNumberElements = document.getElementsByClass("btn_copy_recipe_id");
        String postNumber1 = postNumberElements.attr("recipe_id");
        postNumber = postNumber1;

        //인분
        Elements servingElements = document.getElementsByClass("view2_summary_info1");
        serving = servingElements.text();

        //조리시간
        Elements cookingTimeElements = document.getElementsByClass("view2_summary_info2");
        cookingTime = cookingTimeElements.text();

        //조리 난이도
        Elements cookingLevelElements = document.getElementsByClass("view2_summary_info3");
        cookingLevel = cookingLevelElements.text();

        //재료
        Elements ingredientSections = document.select(".ready_ingre3");

        LinkedHashMap<String, LinkedHashMap<String, String>> ingredientMap2 = new LinkedHashMap<>();

        for (Element ingredientSection : ingredientSections) {
            Elements ingredientLists = ingredientSection.select("ul");

            for (Element ingredientList : ingredientLists) {
                Element ingredientTitleElement = ingredientList.selectFirst(".ready_ingre3_tt");

                if (ingredientTitleElement != null) {
                    String ingredientTitle = ingredientTitleElement.text().trim();
                    LinkedHashMap<String, String> ingredientDetailsMap = new LinkedHashMap<>();
                    ingredientMap2.put(ingredientTitle, ingredientDetailsMap);

                    Elements ingredientItems = ingredientList.select("li");

                    for (Element ingredientItem : ingredientItems) {

                        Element ingredientNameElement = ingredientItem.selectFirst("a");
                        if (ingredientNameElement != null) {
                            String ingredientName = ingredientNameElement.text().trim();
                            String ingredientQuantity = ingredientItem.getElementsByClass("ingre_list_ea").text().trim();
                            ingredientDetailsMap.put(ingredientName, ingredientQuantity);
                        }else {
                            log.info("ingredientNameElement가 널입니다. 요소를 건너뜁니다.");
                        }
                    }
                }
            }


        }
        this.ingredientMap = ingredientMap2;

        //조리 순서
        //id가 stepDiv 로 시작하는 모든 요소 선택
        Elements steps = document.select("div[id^=stepDiv]");

        LinkedHashMap<String, String> stepAndImageMap = new LinkedHashMap<>();

        for (int i = 0; i < steps.size(); i++) {
            String description = document.select("#stepDiv" + i + " .media-body").text().trim();

            //  Element imgElement = document.selectFirst("#stepimg" + 1+i + " img");

            // 이미지 요소가 선택되었는지 확인하고, 선택되었다면 src 속성 값을 가져옴
            //String imageUrl = (imgElement != null) ? imgElement.attr("src") : "";

            String imageUrl = steps.get(i).select("img").attr("src");
            if(imageUrl.isEmpty()){
                imageUrl = "";
            }
            String step  = steps.get(i).select(".media-body").text();

            stepAndImageMap.put(step, imageUrl);
        }

        this.cookingSteps = stepAndImageMap;


        return RecipeDto.builder()
                .postNumber(postNumber)
                .imageUrl(imageUrl)
                .title(title)
                .content(content)
                .servings(serving)
                .cookingTime(cookingTime)
                .cookingLevel(cookingLevel)
                .ingredient(ingredientMap)
                .cookingSteps(cookingSteps)
                .build();
    }



}
