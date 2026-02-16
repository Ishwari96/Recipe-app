package com.id.recipes.recipes_api.model;

public record RecipeDTO (Integer id, String title){


public static RecipeDTO map(RecipeEntity e) {
    return new RecipeDTO(e.getId(), e.getTitle());
}
}