package com.team.RecipeRadar.crawling.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class IngredientDto {

    private String ingredientName;
    private String ingredientQuantity;
    private Map<String, Map<String, String>> ingredientMap = new LinkedHashMap<>();
}

