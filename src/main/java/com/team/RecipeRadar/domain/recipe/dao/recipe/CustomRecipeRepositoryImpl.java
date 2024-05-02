package com.team.RecipeRadar.domain.recipe.dao.recipe;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.RecipeRadar.domain.recipe.domain.Recipe;
import com.team.RecipeRadar.domain.recipe.dto.RecipeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.team.RecipeRadar.domain.recipe.domain.QCookingStep.*;
import static com.team.RecipeRadar.domain.recipe.domain.QIngredient.*;
import static com.team.RecipeRadar.domain.recipe.domain.QRecipe.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomRecipeRepositoryImpl implements CustomRecipeRepository{

    private final JPAQueryFactory queryFactory;

    /**
     * 동적(재료) 무한 스크르롤 페이징 기능
     * @param ingredients       List<String> 재료값
     * @param pageable          페이징
     * @return                  Slice반환
     */
    @Override
    public Slice<RecipeDto> getRecipe(List<String> ingredients, Pageable pageable) {
        
        //동적 쿼리 생성 레시피 list 에서 재료를 하나씩 or like() 문으로 처리
        BooleanBuilder builder = new BooleanBuilder();
        for (String ingredientList : ingredients) {
            builder.or(ingredient.ingredients.like("%"+ingredientList+"%"));
        }

        List<Tuple> result = queryFactory.select(recipe.title, recipe.id, recipe.imageUrl, recipe.likeCount, recipe.cookingTime, recipe.cookingLevel,recipe.people)
                .from(ingredient)
                .join(ingredient.recipe,recipe)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();


        List<RecipeDto> content = result.stream().map(tuple -> RecipeDto.from(tuple.get(recipe.id), tuple.get(recipe.imageUrl), tuple.get(recipe.title), tuple.get(recipe.cookingLevel),
                tuple.get(recipe.people), tuple.get(recipe.cookingTime), tuple.get(recipe.likeCount))).collect(Collectors.toList());
        
        boolean hasNext =false;

        if (content.size() > pageable.getPageSize()){
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content,pageable,hasNext);
    }

    /**
     * 재료 조리순서 를 조인해서 해당 데이터를 가져와서 RecipeDto로 변환
     * @param recipeId
     * @return
     */
    @Override
    public RecipeDto getRecipeDetails(Long recipeId) {

        List<Tuple> details = queryFactory.select(recipe, ingredient.ingredients, cookingStep.steps)
                .from(recipe)
                .join(ingredient).on(ingredient.recipe.id.eq(recipe.id))
                .join(cookingStep).on(cookingStep.recipe.id.eq(recipe.id))
                .where(recipe.id.eq(recipeId)).fetch();


        List<String> cookStep = details.stream().map(tuple -> tuple.get(cookingStep.steps)).collect(Collectors.toList());


        Recipe recipeEntity = details.stream().map(tuple -> tuple.get(recipe)).collect(Collectors.toList()).stream().findFirst().get();

        String ingredients = details.stream().map(tuple -> tuple.get(ingredient.ingredients)).collect(Collectors.toList()).stream().findFirst().get();

        return RecipeDto.of(recipeEntity,cookStep,ingredients);
    }
}
