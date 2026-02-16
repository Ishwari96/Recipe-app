package com.id.recipes.recipes_api.repository;

import com.id.recipes.recipes_api.model.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeServiceRepository extends JpaRepository<RecipeEntity, Integer> {}
