package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.model.RecipeEntity;
import com.id.recipes.recipes_api.repository.RecipeServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface RecipeService {

    public List<RecipeEntity> findAll();

}
