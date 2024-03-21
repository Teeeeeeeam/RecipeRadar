package com.team.RecipeRadar.domain.recipe.application;

import com.team.RecipeRadar.domain.recipe.dto.AddRecipeRequest;
import com.team.RecipeRadar.domain.recipe.domain.Recipe;
import com.team.RecipeRadar.domain.recipe.dto.UpdateRecipeRequest;

import java.util.List;

public interface RecipeService {
    Recipe save(AddRecipeRequest request);

    List<Recipe> findAll();

    Recipe findById(long id);

    void delete(long id);

    Recipe update(long id, UpdateRecipeRequest request);

    List<Recipe> searchRecipes(String query);

    long getRecipeCount();
}