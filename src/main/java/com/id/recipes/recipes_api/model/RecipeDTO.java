package com.id.recipes.recipes_api.model;
import java.util.List;

public record RecipeDTO (long id,
                         String title,
                         boolean vegetarian,
                         int servings,
                         List<String> ingredients,
                         String instructions
){


public static RecipeDTO fromModel(Recipe recipe) {
    return new RecipeDTO(
            recipe.getId(),
            recipe.getTitle(),
            recipe.isVegetarian(),
            recipe.getServings(),
            recipe.getIngredients(),
            recipe.getInstructions()
    );
}
}