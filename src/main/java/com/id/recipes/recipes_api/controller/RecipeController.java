package com.id.recipes.recipes_api.controller;

import com.id.recipes.recipes_api.model.ApiError;
import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.model.RecipeDTO;
import com.id.recipes.recipes_api.model.rest.RecipeRequest;
import com.id.recipes.recipes_api.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class RecipeController {

    private final RecipeService recipeService;
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @Operation(
            summary = "Get all recipes",
            description = "Returns a list of all recipes in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Recipes retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No recipes found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })

    @SuppressWarnings("NullableProblems")
    @GetMapping(path = "/recipes", produces = "application/json")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        List<RecipeDTO> recipes =
                Optional.ofNullable(recipeService.getAll())
                        .orElseGet(List::of);

        return ResponseEntity.ok(recipes);

    }

    /**
     * This method returns unique recipe by id.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> findRecipeById(@PathVariable @Valid long id) {
        return ResponseEntity.ok(recipeService.findById(id));
    }

    /**
     * This method deletes recipe by given ID
     *
     * @param id the id
     */
    @DeleteMapping("/{id}")
    public void deleteRecipeById(@PathVariable @Valid long id) {
        recipeService.deleteById(id);
    }

    /**
     * This method creates recipe with given request body
     *
     * @param recipeRequest the recipe request
     * @return the response entity
     */
    @PostMapping("/recipes")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody Recipe recipeRequest) {
        Recipe saved = recipeService.createRecipe(recipeRequest);
        return ResponseEntity.created(URI.create("/recipes/" + saved.getId())).body(saved);
    }

    /**
     * This method updates recipe of given id with the information provided in
     * request body
     *
     * @param recipeRequest the recipe request
     * @param id            the id
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@Valid @RequestBody Recipe recipeRequest,
                                                       @PathVariable long id) {
        Recipe updated =  recipeService.updateRecipe(id, recipeRequest);
        return ResponseEntity.ok(updated);
    }



}