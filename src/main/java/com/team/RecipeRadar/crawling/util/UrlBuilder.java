package com.team.RecipeRadar.crawling.util;

import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




@Slf4j
public class UrlBuilder {
    private static final String BASE_URL = "https://www.10000recipe.com";
    private static final String RANK_LIST_URL = "https://www.10000recipe.com/recipe/list.html";

    /**
     * 페이징된 레시피 검색 결과의 URL을 추출하여 리스트로 반환합니다.
     *
     * @param query       검색어
     * @param maxPages    최대 페이지 수
     * @return [https://www.10000recipe.com/recipe/list.html?q=닭볶음탕&page=1,...]
     * @throws UrlBuildExceptionHandler Jsoup 연결 중 발생하는 IO 예외
     */
    public List<String> searchPagedRecipeUrlBuilder(String query, int maxPages) throws UrlBuildExceptionHandler {
        try {
            List<String> pagedRecipeUrls = new ArrayList<>();

            for (int page = 1; page <= maxPages; page++) {
                String url = String.format("%s?q=%s&page=%d", RANK_LIST_URL, query, page);
                Document document = Jsoup.connect(url).get();

                Elements selected = document.select("ul.common_sp_list_ul li");

                for (Element recipeElement : selected) {
                    String href = recipeElement.select("div.common_sp_thumb a").attr("href");
                    pagedRecipeUrls.add(BASE_URL + href);
                }

                log.info("{} {}페이지 URL 가공 성공 ",query,page);
            }
            return pagedRecipeUrls;

        } catch (IOException e) {
            throw new UrlBuildExceptionHandler("크롤링 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 레시피 랭킹 페이지에서 각 레시피의 URL을 추출하여 리스트로 반환합니다.
     *
     * @return [https://www.10000recipe.com/recipe/list.html?q=닭볶음탕,...]
     * @throws UrlBuildExceptionHandler Jsoup 연결 중 발생하는 IO 예외
     */

    public List<String> searchRankRecipeUrlBuilder() throws UrlBuildExceptionHandler {

        try {

            final String RANK_LIST_URL = "https://www.10000recipe.com/ranking/home_new.html?rtype=k&dtype=d";
            Document document = Jsoup.connect(RANK_LIST_URL).get();

            Elements selected = document.select("ul.goods_best3_1 li");

            List<String> rankListUrls = new ArrayList<>();
            for (Element recipeElement : selected) {
                String href = recipeElement.select("div.best_cont a").attr("href");
                rankListUrls.add(BASE_URL + href);
            }

            return rankListUrls;



        } catch (IOException e) {

            throw new UrlBuildExceptionHandler("레시피 URL을 검색하는 중 오류가 발생했습니다.",e);
        }

    }

    /**
     * 조회된 레시피에서 상세레시피게시글 주소를 빌드하고 리스트로 담아서 리턴한다.
     *
     * @return [https://www.10000recipe.com/profile/recipe.html?uid=bhs1009833,...]
     * @throws UrlBuildExceptionHandler Jsoup 연결 중 발생하는 IO 예외
     */
    public List<String> recipeDetailedPostUrlBuilder(List<String> rankListUrls) throws UrlBuildExceptionHandler {
        try {
            ArrayList<String> recipeDetailPostEndPointUrls = new ArrayList<>();

            log.info("rankListUrls Size : ={}",rankListUrls.size());
            for (int i = 0; i < rankListUrls.size(); i++) {
                Document document = Jsoup.connect(rankListUrls.get(i)).get();
                Elements selected = document.select( "ul.common_sp_list_ul li");

                for (Element recipeDetailElement : selected) {
                    String href = recipeDetailElement.select("div.common_sp_thumb a").attr("href");
                    recipeDetailPostEndPointUrls.add(BASE_URL+href);
                }

            }



            return recipeDetailPostEndPointUrls;

        }catch (IOException e){

            throw new UrlBuildExceptionHandler("레시피 상세글 URL을 빌드하는 도중 오류가 발생했습니다.",e);
        }


    };




}
