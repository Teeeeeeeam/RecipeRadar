package com.team.RecipeRadar.crawling.util;

import com.team.RecipeRadar.crawling.dto.RecipeDto;
import com.team.RecipeRadar.crawling.entity.CookingStep;
import com.team.RecipeRadar.crawling.entity.Ingredient;
import com.team.RecipeRadar.crawling.entity.Recipe;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class EntityMappers {


    private RecipePostScrapers recipePostScraper;

    public Recipe mapToRecipeEntities(RecipeDto recipeDto) {

        return Recipe.builder()
                .postNumber(recipeDto.getPostNumber())
                .imageUrl(recipeDto.getImageUrl())
                .title(recipeDto.getTitle())
                .content(recipeDto.getContent())
                .servings(recipeDto.getServings())
                .cookingTime(recipeDto.getCookingTime())
                .cookingLevel(recipeDto.getCookingLevel())
                .build();
    }


   public List<Ingredient> mapToIngredientEntities(LinkedHashMap<String, LinkedHashMap<String,String>> ingredientMap, String postNumber,Long recipeId) {

       List<Ingredient> ingredientList = new ArrayList<>();
       StringBuilder ingredientAndQuantityBuild = new StringBuilder();

       ingredientMap.forEach((key,value) ->{
           String ingredientTitle = key;

           value.forEach((ingredientName,quantity)->{
               ingredientAndQuantityBuild.append(ingredientName).append(":").append(quantity).append(",");
               //재료 마지막에 추가된 "," 지우기

           });
           //StringIndexOutOfBoundsException 방지
           if(!ingredientAndQuantityBuild.isEmpty()) {
               String ingredientAndQuantity = String.valueOf(ingredientAndQuantityBuild).substring(0, ingredientAndQuantityBuild.length() - 1);
               Ingredient ingredient = Ingredient.builder()
                       .recipeId(recipeId)
                       .postNumber(postNumber)
                       .ingredientTitle(ingredientTitle)
                       .ingredientAndQuantity(ingredientAndQuantity)
                       .build();
               ingredientList.add(ingredient);
           }
       });
       return ingredientList;
   }


    public List<CookingStep> mapToCookingStepEntities(LinkedHashMap<String, String> cookingSteps,String postNumber,Long recipeId) {

        List<CookingStep> cookingStepList =  new ArrayList<>();
        AtomicInteger stepNumber = new AtomicInteger(0);

        cookingSteps.forEach((key ,value) -> {
            String explanation = key;
            String imageUrl =value;

            // stepNumber를 증가시키고 현재 값을 가져옴
            int currentStepNumber = stepNumber.incrementAndGet();

            CookingStep cookingStep = CookingStep.builder()
                    .recipeId(recipeId)
                    .postNumber(postNumber)
                    .stepNumber(currentStepNumber)
                    .explanation(explanation)
                    .imageUrl(imageUrl)
                    .build();

            cookingStepList.add(cookingStep);
        });


          return  cookingStepList;
    }


}
