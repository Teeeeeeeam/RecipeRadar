package com.team.RecipeRadar.domain.recipe.dao;

import com.team.RecipeRadar.domain.recipe.domain.Recipe;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<Recipe> recipeSearch(List<String> condition);
}
