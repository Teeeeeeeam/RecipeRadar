package com.team.RecipeRadar.crawling;

import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import com.team.RecipeRadar.crawling.util.CrawlingUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;


class UrlBuilderTest {

    @Test
    @DisplayName("페이징된 레시피 검색 결과의 URL을 추출하여 400개 크기의 리스트로 반환합니다.")
    void test() throws UrlBuildExceptionHandler {
        CrawlingUtil urlBuilder = new CrawlingUtil();

        List<String> strings = urlBuilder.searchPagedRecipeUrlBuilder("잡채", 10);

        System.out.println(strings);

        Assertions.assertThat(strings.size()).isEqualTo(400);

    }

    @Test
    @DisplayName("LinkedHashMap Test")
    void test2() {
        new LinkedHashMap<String, String>();

    }

}

