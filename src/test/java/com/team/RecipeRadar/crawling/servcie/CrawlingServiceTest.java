package com.team.RecipeRadar.crawling.servcie;

import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import com.team.RecipeRadar.crawling.util.EntityMappers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;


@SpringBootTest
class CrawlingServiceTest {

    @Autowired
    private CrawlingService crawlingService;

    @Autowired
    private EntityMappers entityMapper;
    @Test
    @DisplayName("레시피 스크랩후 디비저장 통합테스트")
    void tset() throws UrlBuildExceptionHandler {

        crawlingService.startCrawling();

    }

    @Test
    @DisplayName("linkedHashMapTest")
    void tset2() throws UrlBuildExceptionHandler {
        LinkedHashMap<String, LinkedHashMap<String, String>> map1 = new LinkedHashMap<>();
        LinkedHashMap<String, String> map2 = new LinkedHashMap<>();
        LinkedHashMap<String, String> map3 = new LinkedHashMap<>();

        map1.put("닭복음탕 재료", map2);
        map1.put("앵념재료 재료", map3);


        map2.put("닭 팩", "1마리");
        map2.put("감자", "4개");
        map2.put("당근", "1/2개");
        map2.put("양파", "1개");
        map2.put("홍고추", "1개");
        map2.put("매운청양고추", "1개");
        map2.put("물", "7컵");

        map3.put("재료1", "1마리");
        map3.put("재료2", "4개");
        map3.put("재료3", "1/2개");
        map3.put("재료4", "1개");
        map3.put("재료5", "1개");
        map3.put("재료6", "1개");
        map3.put("재료7", "7컵");


        System.out.println(map1.toString());

        System.out.println("===============================");
        for (var title : map1.keySet()) {
           // System.out.println(title);
        }

    /*    map1.forEach((key,value)->{
            System.out.println(key);
            value.forEach((ingredient,quantity)->{
                System.out.println(ingredient);
                System.out.println(quantity);
            });
            System.out.println("===============");
        });
*/
      //  List<Ingredient> ingredients = entityMapper.mapToIngredientEntities(map1, "1234");
      //  List<Ingredient> ingredients = entityMapper.mapToIngredientEntities(map1, "1234");

        //ingredients.forEach(ingredient -> System.out.println(ingredient.toString()));
        //ingredients.forEach(ingredient -> System.out.println(ingredient.toString()));


    }


    }


