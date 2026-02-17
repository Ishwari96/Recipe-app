package com.id.recipes.recipes_api.controller;

import com.id.recipes.recipes_api.model.RecipeDTO;
import com.id.recipes.recipes_api.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @SuppressWarnings("NullableProblems")
    @GetMapping(path = "/recipes", produces = "application/json")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        List<RecipeDTO> recipes =
                Optional.ofNullable(recipeService.getAll())
                        .orElseGet(List::of);

        return ResponseEntity.ok(recipes);

    }


}