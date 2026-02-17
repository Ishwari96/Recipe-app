package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.model.RecipeDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface RecipeService {

    /**
     * Gets the all.
     *
     * @return the all
     */
    List<RecipeDTO> getAll();

    /**
     * Find by id.
     *
     * @param recipeId the recipe id
     * @return the optional
     */
    RecipeDTO findById(long recipeId);

    /**
     * Delete recipe.
     *
     * @param recipeId the recipe id
     */
    void deleteById(@Valid long recipeId);

    /**
     * Creates the recipe.
     *
     * @param recipe the recipe
     * @return the recipe
     */
    Recipe createRecipe(Recipe recipe);

    /**
     * Update recipe.
     *
     * @param recipe the recipe
     * @return the recipe
     */
    Recipe updateRecipe(long id, Recipe recipe);

}
