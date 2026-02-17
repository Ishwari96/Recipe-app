package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.model.RecipeDTO;

import java.util.List;

public interface RecipeService {

    /**
     * Gets the all.
     *
     * @return the all
     */
    public List<RecipeDTO> getAll();

}
