package com.id.recipes.recipes_api.model.dto;
import com.id.recipes.recipes_api.model.Recipe;

import java.util.Set;
import java.util.stream.Collectors;

public record RecipeDTO (long id,
                         String title,
                         boolean vegetarian,
                         int servings,
                         Set<IngredientDTO> ingredients,
                         String instructions
){


public static RecipeDTO fromModel(Recipe recipe) {

    Set<IngredientDTO> ingredients;
    ingredients = recipe.getIngredients().stream().map(IngredientDTO::fromModel).collect(Collectors.toUnmodifiableSet());
    return new RecipeDTO(
            recipe.getId(),
            recipe.getTitle(),
            recipe.isVegetarian(),
            recipe.getServings(),
            ingredients,
            recipe.getInstructions()
    );
}
}