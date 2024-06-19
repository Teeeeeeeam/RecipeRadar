package com.team.RecipeRadar.domain.recipe.dto.response;

import com.team.RecipeRadar.domain.recipe.dto.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecipeNormalPageResponse {

    private List<RecipeDto> recipes;
    private int totalPage;
    private long totalElements;
}