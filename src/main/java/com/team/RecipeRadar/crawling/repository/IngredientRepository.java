package com.team.RecipeRadar.crawling.repository;


import com.team.RecipeRadar.crawling.entity.Ingredient;
import com.team.RecipeRadar.crawling.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

  //  public void insertIgnore();
}
