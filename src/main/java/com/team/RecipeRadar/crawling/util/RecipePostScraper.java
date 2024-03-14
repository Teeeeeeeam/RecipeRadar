package com.team.RecipeRadar.crawling.util;

import com.team.RecipeRadar.crawling.dto.CookingStepDTO;
import com.team.RecipeRadar.crawling.dto.RecipeDto;
import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Slf4j
public class RecipePostScraper {

    private Integer postNumber;

    private String imageUrl;

    private String title;

    private String content;

    private String serving;

    private String cookingTime;

    private String cookingLevel;

    Map<String, Map<String, String>> ingredientMap = new LinkedHashMap<>();
    private LinkedHashMap<String, String> cookingSteps = new LinkedHashMap<>();


    UrlBuilder urlBuilder = new UrlBuilder();

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

    public List<RecipeDto> postScrap() throws UrlBuildExceptionHandler {
        // 현재 자원 코어수 * 2 스레드풀 생성
        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(2 * coreCount);

        List<String> recipePostUrls = urlBuilder.searchPagedRecipeUrlBuilder("닭복음탕", 10);

        List<CompletableFuture<Void>> futures = recipePostUrls.stream()
                .map(url -> CompletableFuture.runAsync(() -> {
                    try {

                        Document document = Jsoup.connect(url).get();
                        //이미지
                        Element mainThumbsImage = document.getElementById("main_thumbs");
                        imageUrl = mainThumbsImage.attr("src");
                        //log.info("imageUrl={}", imageUrl);

                        //제목
                        Elements elements = document.getElementsByClass("view2_summary st3");
                        title = elements.select("h3").text();

                        //내용
                        Elements contentElements = document.getElementsByClass("view2_summary_in");
                        content = contentElements.html();

                        //게시글 번호
                        Elements postNumberElements = document.getElementsByClass("btn_copy_recipe_id");
                        String postNumber1 = postNumberElements.attr("recipe_id");
                        postNumber = Integer.parseInt(postNumber1);

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

                        Map<String, Map<String, String>> ingredientMap2 = new LinkedHashMap<>();

                        for (Element ingredientSection : ingredientSections) {
                            Elements ingredientLists = ingredientSection.select("ul");

                            for (Element ingredientList : ingredientLists) {
                                Element ingredientTitleElement = ingredientList.selectFirst(".ready_ingre3_tt");

                                if (ingredientTitleElement != null) {
                                    String ingredientTitle = ingredientTitleElement.text().trim();
                                    Map<String, String> ingredientDetailsMap = new LinkedHashMap<>();
                                    ingredientMap2.put(ingredientTitle, ingredientDetailsMap);

                                    Elements ingredientItems = ingredientList.select("li");

                                    for (Element ingredientItem : ingredientItems) {

                                        Element ingredientNameElement = ingredientItem.selectFirst("a");
                                        if (ingredientNameElement != null) {
                                            String ingredientName = ingredientNameElement.text().trim();
                                            String ingredientQuantity = ingredientItem.select(".ingre_unit").text().trim();
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

                        for (int i = 1; i <= steps.size(); i++) {
                            String description = document.select("#stepDiv" + i + " .media-body").text().trim();

                            Element imgElement = document.selectFirst("#stepimg" + i + " img");

                            // 이미지 요소가 선택되었는지 확인하고, 선택되었다면 src 속성 값을 가져옵니다.
                            String imageUrl = (imgElement != null) ? imgElement.attr("src") : "image null";

                            this.cookingSteps.put(description, imageUrl);
                        }



                        RecipeDto recipeDto = RecipeDto.builder()
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

                        synchronized (recipeDtoList) {
                            recipeDtoList.add(recipeDto);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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



}
