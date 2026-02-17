package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.model.RecipeDTO;
import com.id.recipes.recipes_api.repository.RecipeServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService{

    /** The recipe repository. */
    @Autowired
    RecipeServiceRepository recipeServiceRepository;

    /**
     * Gets the all.
     *
     * @return the all recipes
     */
    @Override
    public List<RecipeDTO> getAll() {
        return recipeServiceRepository.findAll().stream().map(RecipeDTO::fromModel).collect(Collectors.toList());
    }

}
