package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.exception.RecipeNotFoundException;
import com.id.recipes.recipes_api.model.Ingredient;
import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.model.dto.RecipeDTO;
import com.id.recipes.recipes_api.utility.SearchCriteria;
import com.id.recipes.recipes_api.model.rest.RecipeRequest;
import com.id.recipes.recipes_api.repository.RecipeRepository;
import com.id.recipes.recipes_api.utility.RecipeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        try {
            recipeRepository.deleteById(recipeId);
        }  catch (EmptyResultDataAccessException ex) {
            throw new RecipeNotFoundException("Recipe not found for id=" + recipeId);
        }

    }

    /**
     * Creates the recipe.
     *
     * @param recipeRequest the recipe request received
     * @return the recipe
     */
    @SuppressWarnings("unchecked")
    @Override
    public Recipe createRecipe(RecipeRequest recipeRequest) {
        Recipe createRecipe = new Recipe();
        createRecipe.setTitle(recipeRequest.getTitle());
        createRecipe.setVegetarian(recipeRequest.isVegetarian());
        createRecipe.setServings(recipeRequest.getNumberOfPerson());
        createRecipe.setInstructions(recipeRequest.getCookingInstruction());
        if(recipeRequest.getIngredients() != null) {

            createRecipe.setIngredients(Set.copyOf(recipeRequest.getIngredients()));
        }
        return recipeRepository.save(createRecipe);
    }

    /**
     * Update recipe.
     *
     * @param updatedRecipe the recipe
     * @return the recipe
     */
    @Override
    public Recipe updateRecipe(long id, RecipeRequest updatedRecipe) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id "+ id ));

        //update fields (selective)
        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setVegetarian(updatedRecipe.isVegetarian());
        existingRecipe.setServings(updatedRecipe.getNumberOfPerson());
        existingRecipe.setInstructions(updatedRecipe.getCookingInstruction());

        //normalize and set ingredients
        for (Ingredient ingredient : updatedRecipe.getIngredients()) {
            this.updateIngredient(ingredient, existingRecipe.getIngredients());
        }

        return recipeRepository.save(existingRecipe);
    }

    /**
     * Search criteria
     * @param searchCriteria received from front end
     * @return list of recipe
     */
    public List<Recipe> findBySearchCriteria(SearchCriteria searchCriteria) {
        List<Recipe> entities =  recipeRepository.findAll(RecipeSpecification.search(searchCriteria));
        return entities;
    }

    private void updateIngredient(Ingredient ingredientParam, Set<Ingredient> listIngredients) {
        for (Ingredient ingredient : listIngredients) {
            if (ingredient.getId() == ingredientParam.getId()) {
                ingredient.setDescription(ingredientParam.getDescription());
                ingredient.setName(ingredientParam.getName());
            }
        }
    }
}
