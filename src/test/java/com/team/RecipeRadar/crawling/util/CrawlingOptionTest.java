package com.team.RecipeRadar.crawling.util;

import com.beust.jcommander.JCommander;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class CrawlingOptionTest {

    @Test
    @DisplayName("-ingredients 옵션은 여러가지 재료를 배열로 입력 받을 수 있다.")
    void test() {
        String[] args = {"-ingredients","재료1","재료2","재료3"};

        CrawlingOption crawlingOption = new CrawlingOption();

        JCommander.newBuilder()
                .addObject(crawlingOption)
                .build()
                .parse(args);



    }

}