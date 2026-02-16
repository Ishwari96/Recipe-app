package com.id.recipes.recipes_api.controller;

import com.id.recipes.recipes_api.model.RecipeDTO;
import com.id.recipes.recipes_api.model.RecipeEntity;
import com.id.recipes.recipes_api.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.Map;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(path = "/recipes", produces = "application/json")
    public Map<String, Object> list() {
        var all = recipeService.findAll().stream()
                .sorted(Comparator.comparing(RecipeEntity::getId))
                .map(RecipeDTO::map).toList();
        return Map.of("recipes", all);
    }

}
