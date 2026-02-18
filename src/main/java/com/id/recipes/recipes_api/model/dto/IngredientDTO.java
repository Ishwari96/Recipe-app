package com.id.recipes.recipes_api.model.dto;

import com.id.recipes.recipes_api.model.Ingredient;

public record IngredientDTO (long id, String name, String description){

    public static IngredientDTO fromModel(Ingredient ingredient) {
        return new IngredientDTO(ingredient.getId(), ingredient.getName(), ingredient.getDescription());
    }

}
