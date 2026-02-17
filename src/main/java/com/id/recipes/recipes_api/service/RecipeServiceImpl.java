package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.model.RecipeEntity;
import com.id.recipes.recipes_api.repository.RecipeServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService{

    /** The recipe repository. */
    @Autowired
    RecipeServiceRepository recipeServiceRepository;

    public List<RecipeEntity> findAll() {
        return recipeServiceRepository.findAll();
    }

}
