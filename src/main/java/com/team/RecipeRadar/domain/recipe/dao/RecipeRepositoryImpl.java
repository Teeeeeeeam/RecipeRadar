package com.team.RecipeRadar.domain.recipe.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.RecipeRadar.domain.recipe.domain.QRecipe;
import com.team.RecipeRadar.domain.recipe.domain.Recipe;
import lombok.RequiredArgsConstructor;
import java.util.List;
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Recipe> recipeSearch(List<String> condition) {

        QRecipe recipe = QRecipe.recipe;
        return   jpaQueryFactory
                .selectFrom(recipe)
                .limit(5)
                .fetch();
    }
}
