package com.id.recipes.recipes_api.repository;

import com.id.recipes.recipes_api.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeServiceRepository extends JpaRepository<Recipe, Integer> {}
