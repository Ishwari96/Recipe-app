package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.exception.RecipeNotFoundException;
import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.model.RecipeDTO;
import com.id.recipes.recipes_api.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService{

    /** The recipe repository. */
    @Autowired
    RecipeRepository recipeRepository;

    /**
     * Gets the all.
     *
     * @return the all recipes
     */
    @Override
    public List<RecipeDTO> getAll() {
        return recipeRepository.findAll().stream().map(RecipeDTO::fromModel).collect(Collectors.toList());
    }

    /**
     * Find by id.
     *
     * @param recipeId the recipe id
     * @return the optional
     */
    @Override
    public RecipeDTO findById(long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty())
            throw new RecipeNotFoundException("recipe is not found for recipeId=" + recipeId);

        return RecipeDTO.fromModel(recipe.get());
    }

    /**
     * Delete recipe.
     *
     * @param recipeId the recipe id
     */
    @Override
    public void deleteById(long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    /**
     * Creates the recipe.
     *
     * @param recipe the recipe
     * @return the recipe
     */
    @Override
    public Recipe createRecipe(Recipe recipe) {
        if(recipe.getIngredients() != null) {
            recipe.setIngredients(normalizeList(recipe.getIngredients()));
        }
        return recipeRepository.save(recipe);
    }

    /**
     * Update recipe.
     *
     * @param updatedRecipe the recipe
     * @return the recipe
     */
    @Override
    public Recipe updateRecipe(long id, Recipe updatedRecipe) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id " ));

        //update fields (selective)
        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setVegetarian(updatedRecipe.isVegetarian());
        existingRecipe.setServings(updatedRecipe.getServings());
        existingRecipe.setInstructions(updatedRecipe.getInstructions());

        //normalize and set ingredients
        existingRecipe.setIngredients(normalizeList(updatedRecipe.getIngredients()));
        return recipeRepository.save(updatedRecipe);
    }

    private List<String> normalizeList(List<String> list) {
        if(list == null) return null;
        return list.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toList());
    }
}
