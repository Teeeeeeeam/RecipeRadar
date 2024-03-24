package com.team.RecipeRadar.crawling.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;

@Getter
@Setter
@ToString
public class CookingStepDto {
    private LinkedHashMap<String, String> cookingSteps = new LinkedHashMap<>();
}

