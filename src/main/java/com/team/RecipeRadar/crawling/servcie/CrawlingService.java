package com.team.RecipeRadar.crawling.servcie;

import com.team.RecipeRadar.crawling.dto.RecipeDto;
import com.team.RecipeRadar.crawling.entity.CookingStep;
import com.team.RecipeRadar.crawling.entity.Ingredient;
import com.team.RecipeRadar.crawling.entity.Recipe;
import com.team.RecipeRadar.crawling.excption.UrlBuildExceptionHandler;
import com.team.RecipeRadar.crawling.repository.CookingStepRepository;
import com.team.RecipeRadar.crawling.repository.IngredientRepository;
import com.team.RecipeRadar.crawling.repository.RecipeRepository;
import com.team.RecipeRadar.crawling.util.EntityMappers;
import com.team.RecipeRadar.crawling.util.RecipePostScrapers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CrawlingService {


    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CookingStepRepository cookingStepRepository;

    private final RecipePostScrapers recipePostScraper;

    private final EntityMappers entityMapper;

    public void startCrawling(CrawlingOption crawlingOption) throws UrlBuildExceptionHandler {
        List<RecipeDto> recipeDtoList = new ArrayList<>();

        if(crawlingOption.getRecipe() == null){
            recipeDtoList = recipePostScraper.repeatedWorkScrap( );
        }else {
            List<String> recipes = crawlingOption.getRecipe();
            Integer maxPage = crawlingOption.getPage();

            for(String recipe :recipes){
               recipeDtoList = recipePostScraper.postScrap(recipe,maxPage);
            }
        }



        for (RecipeDto recipeDto : recipeDtoList) {

            Recipe recipe = entityMapper.mapToRecipeEntities(recipeDto);
            Recipe savedRecipe = recipeRepository.save(recipe);

            Long recipeId = savedRecipe.getId();

            List<Ingredient> ingredients = entityMapper.mapToIngredientEntities(recipeDto.getIngredient(), recipe.getPostNumber(),recipeId);
            ingredientRepository.saveAll(ingredients);

            List<CookingStep> cookingSteps = entityMapper.mapToCookingStepEntities(recipeDto.getCookingSteps(), recipe.getPostNumber(),recipeId);
            cookingStepRepository.saveAll(cookingSteps);


        }


    }



}
