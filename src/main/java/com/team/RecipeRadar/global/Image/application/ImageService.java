package com.team.RecipeRadar.global.Image.application;

import com.team.RecipeRadar.domain.recipe.domain.Recipe;
import com.team.RecipeRadar.global.Image.domain.UploadFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {

    void saveRecipeImg(Recipe recipe, UploadFile uploadFile);

    Optional<byte[]> getImageByRecipeId(Long recipeId) throws IOException;
}
