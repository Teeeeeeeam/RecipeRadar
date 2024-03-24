package com.team.RecipeRadar.crawling;

import com.team.RecipeRadar.crawling.dto.RecipeDto;
import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import com.team.RecipeRadar.crawling.util.RecipePostScrapers;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
class RecipePostScraperTest {


    @Test
    @DisplayName("상세페이지 레시피 데이터 스크랩 테스트")
    void test3() throws UrlBuildExceptionHandler {

        //given

        RecipePostScrapers recipePostScraper = new RecipePostScrapers();
        //when//then
        List<RecipeDto> recipeDtos = recipePostScraper.postScrap("잡채" ,10);

        for (RecipeDto recipeDto : recipeDtos) {

          //  System.out.println(recipeDto.toString());
           // break;
        }
        System.out.println(recipeDtos.get(0).toString());

        Assertions.assertThat(recipeDtos.size()).isEqualTo(400);


    }

    @Test
    @DisplayName("이미지 url 스크랩 테스트")
    void test2() throws IOException {

        for (int i = 1; i <= 400; i++) {

            String url = "https://www.10000recipe.com/recipe/6889570";
            //given
            Document document = Jsoup.connect(url).get();
            Element mainThumbsImage = document.getElementById("main_thumbs");
            String srcAttribute = mainThumbsImage.attr("src");

            //when
            System.out.println("ddd : " + srcAttribute);
            //then
        }
    }


    @Test
    @DisplayName("제목, 내용, 게시글 번호,  스크랩 테스트")
    void test5() throws IOException {
        String url = "https://www.10000recipe.com/recipe/6906655";
        //given
        Document document = Jsoup.connect(url).get();
        Elements titleElements = document.getElementsByClass("view2_summary st3");
        String title = titleElements.select("h3").text();
        //System.out.println(title);


        Elements contentElements = document.getElementsByClass("view2_summary_in");
        String content = contentElements.html();
        //System.out.println(content);


        Elements postNumberElements = document.getElementsByClass("btn_copy_recipe_id");
        String postNumber1 = postNumberElements.attr("recipe_id");
        int postNumber = Integer.parseInt(postNumber1);
        System.out.println(postNumber);

    }

    @Test
    @DisplayName("인분,조리시간,난이도  스크랩 테스트")
    void test6() throws IOException {
        String url = "https://www.10000recipe.com/recipe/6906655";
        //given
        Document document = Jsoup.connect(url).get();
        Elements servingElements = document.getElementsByClass("view2_summary_info1");
        String serving = servingElements.text();
        System.out.println(serving);


        Elements cookingTimeElements = document.getElementsByClass("view2_summary_info2");
        String cookingTime = cookingTimeElements.text();
        System.out.println(cookingTime);


        Elements cookingLevelElements = document.getElementsByClass("view2_summary_info3");
        String cookingLevel = cookingLevelElements.text();
        System.out.println(cookingLevel);

    }


    @Test
    @DisplayName("레시피 재료들 스크랩 테스트 최종")
    void test8() throws IOException {
        String url ="";

            url = "https://www.10000recipe.com/recipe/6889570";

        Document document = Jsoup.connect(url).get();
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
                            String ingredientQuantity = ingredientItem.getElementsByClass("ingre_list_ea").text().trim();
                            ingredientDetailsMap.put(ingredientName, ingredientQuantity);
                            System.out.println(ingredientName);
                            System.out.println(ingredientQuantity);
                        }else {
                            System.out.println(("ingredientNameElement가 널입니다. 요소를 건너뜁니다."));
                        }
                    }
                }
            }


        }


    }

    @Test
    @DisplayName("레시피 조리순서 스크랩 테스트")
    void test9() throws IOException {
        String URL = "https://www.10000recipe.com/recipe/6856705";
        try {
            Document doc = Jsoup.connect(URL).get();
            //id가 stepDiv 로 시작하는 모든 요소 선택
            Elements steps = doc.select("div[id^=stepDiv]");

            LinkedHashMap<String, String> stepImageMap = new LinkedHashMap<>();

            for (int i = 1; i <= steps.size(); i++) {
                String description = doc.select("#stepDiv" + i + " .media-body").text().trim();

                Element imgElement = doc.selectFirst("#stepimg" + i + " img");

                // 이미지 요소가 선택되었는지 확인하고, 선택되었다면 src 속성 값을 가져옴
                String imageUrl = (imgElement != null) ? imgElement.attr("src") : "";

                stepImageMap.put(description, imageUrl);
            }

            for (Map.Entry<String, String> entry : stepImageMap.entrySet()) {
                System.out.println("Step: " + entry.getKey());
                System.out.println("Image URL: " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



