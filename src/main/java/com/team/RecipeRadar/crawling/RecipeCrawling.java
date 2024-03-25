package com.team.RecipeRadar.crawling;

import com.beust.jcommander.JCommander;
import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import com.team.RecipeRadar.crawling.servcie.CrawlingService;
import com.team.RecipeRadar.crawling.util.CrawlingOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@SpringBootApplication
public class RecipeCrawling {

    /**
     * 레시피 크롤링 시작점 입니다.
     * @param args
     * @throws UrlBuildExceptionHandler
     */
    public static void main(String[] args) throws UrlBuildExceptionHandler {

        CrawlingOption crawlingOption = new CrawlingOption();

        JCommander.newBuilder()
                .addObject(crawlingOption)
                .build().parse(args);



        log.info("crawling start : recipe={}, page={}",crawlingOption.getRecipe(),crawlingOption.getPage());


        ConfigurableApplicationContext context = SpringApplication.run(RecipeCrawling.class, args);

        CrawlingService crawlingService = context.getBean(CrawlingService.class);

        try{
            crawlingService.startCrawling(crawlingOption);
            log.info("레시피 크롤링 완료");
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    }


