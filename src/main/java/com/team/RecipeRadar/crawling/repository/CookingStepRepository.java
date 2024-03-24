package com.team.RecipeRadar.crawling.repository;


import com.team.RecipeRadar.crawling.entity.CookingStep;
import com.team.RecipeRadar.crawling.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookingStepRepository extends JpaRepository<CookingStep,Long> {

  //  public void insertIgnore();
}
