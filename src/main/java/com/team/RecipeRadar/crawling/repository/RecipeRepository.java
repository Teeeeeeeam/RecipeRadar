package com.team.RecipeRadar.crawling.repository;


import com.team.RecipeRadar.crawling.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

  //  public void insertIgnore();
}
