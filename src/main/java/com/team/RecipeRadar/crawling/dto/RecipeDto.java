package com.team.RecipeRadar.crawling.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class RecipeDto {


    private Integer postNumber;

    private String imageUrl;

    private String title;

    private String content;

    private String servings;

    private String cookingTime;

    private String cookingLevel;

    private Map<String,Map<String,String>> ingredient;//["양념재료":["돼지고기":"600g","대파":"1/2대"]]

    private LinkedHashMap<String, String> cookingSteps;

    @Builder
    public RecipeDto( Integer postNumber, String imageUrl, String title, String content, String servings, String cookingTime, String cookingLevel, Map<String, Map<String, String>> ingredient, LinkedHashMap<String, String> cookingSteps) {
        this.postNumber = postNumber;
        this.imageUrl = imageUrl;
        this.title = title;
        this.content = content;
        this.servings = servings;
        this.cookingTime = cookingTime;
        this.cookingLevel = cookingLevel;
        this.ingredient = ingredient;
        this.cookingSteps = cookingSteps;
    }
}
