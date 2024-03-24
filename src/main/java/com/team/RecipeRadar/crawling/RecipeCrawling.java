package com.team.RecipeRadar.crawling;

import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import com.team.RecipeRadar.crawling.servcie.CrawlingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class RecipeCrawling {

    /**
     * 레시피 크롤링 시작점 입니다.
     * @param args
     * @throws UrlBuildExceptionHandler
     */
    public static void main(String[] args) throws UrlBuildExceptionHandler {

        ConfigurableApplicationContext context = SpringApplication.run(RecipeCrawling.class, args);

        CrawlingService crawlingService = context.getBean(CrawlingService.class);

        crawlingService.startCrawling();

    }
    }


