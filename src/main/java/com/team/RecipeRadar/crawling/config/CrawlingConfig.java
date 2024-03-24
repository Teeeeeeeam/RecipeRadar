package com.team.RecipeRadar.crawling.config;

import com.team.RecipeRadar.crawling.repository.CookingStepRepository;
import com.team.RecipeRadar.crawling.repository.IngredientRepository;
import com.team.RecipeRadar.crawling.repository.RecipeRepository;
import com.team.RecipeRadar.crawling.servcie.CrawlingService;
import com.team.RecipeRadar.crawling.util.EntityMappers;
import com.team.RecipeRadar.crawling.util.RecipePostScrapers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration


public class CrawlingConfig {

    private final IngredientRepository ingredientRepository;

    private final CookingStepRepository cookingStepRepository;

    private final RecipeRepository recipeRepository;

    private final RecipePostScrapers recipePostScraper;

    private final EntityMappers entityMapper;



    @Bean
    public CrawlingService crawlingService() {
        return new CrawlingService(recipeRepository, ingredientRepository, cookingStepRepository, recipePostScraper, entityMapper);
    }


}
