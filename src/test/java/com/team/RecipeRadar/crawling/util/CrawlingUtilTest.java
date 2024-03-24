package com.team.RecipeRadar.crawling.util;

import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class CrawlingUtilTest {


    private static final String RECIPE_RANK_LIST_URL = "https://www.10000recipe.com/ranking/home_new.html?rtype=k&dtype=d";
    @Test
    @DisplayName("레시피 검색 랭킹 리스트로 가져온다.")
    void test() throws UrlBuildExceptionHandler {

            try {
                List<String> recipeRankList = new ArrayList<>();

                Document document = Jsoup.connect(RECIPE_RANK_LIST_URL).get();
                Elements elements = document.getElementsByClass("best_cont");


                elements.forEach(element -> {
                    String p = element.select("a").text();
                    recipeRankList.add(p);
                });

                recipeRankList.forEach(System.out::println);

            } catch (IOException e) {
                throw new UrlBuildExceptionHandler("레시피 검색 순위를 스크랩 하다 오류가 발생 하였습니다.", e);
            }


        }
    }


