package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.model.dto.RecipeDTO;
import com.id.recipes.recipes_api.utility.SearchCriteria;
import com.id.recipes.recipes_api.model.rest.RecipeRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

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
     * @param recipeRequest the recipeRequest body
     * @return the recipe
     */
    Recipe createRecipe(@RequestBody RecipeRequest recipeRequest);

    /**
     * Update recipe.
     *
     * @param recipeRequest the recipe request
     * @return the recipe
     */
    Recipe updateRecipe(long id, RecipeRequest recipeRequest);

    /**
     * Search criteria
     * @param searchCriteria required
     * @return recipe based on requirement
     */
    List<Recipe> findBySearchCriteria(SearchCriteria searchCriteria);
}
