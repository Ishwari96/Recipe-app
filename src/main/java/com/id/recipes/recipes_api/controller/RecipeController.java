package com.id.recipes.recipes_api.controller;

import com.id.recipes.recipes_api.model.ApiError;
import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.model.dto.RecipeDTO;
import com.id.recipes.recipes_api.utility.SearchCriteria;
import com.id.recipes.recipes_api.model.rest.RecipeRequest;
import com.id.recipes.recipes_api.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
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
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })

    /**
     * fetch all recipes
     */
    @GetMapping(path = "/recipes", produces = "application/json")
    public ResponseEntity<List<RecipeDTO>> getRecipes() {
        List<RecipeDTO> recipes =
                Optional.ofNullable(recipeService.getAll())
                        .orElseGet(List::of);

        return ResponseEntity.ok(recipes);

    }

    @Operation(summary = "Get recipe by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipe found"),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })

    /**
      This method returns unique recipe by id.

      @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> findRecipeById(@PathVariable @Valid long id) {
        return ResponseEntity.ok(recipeService.findById(id));
    }


    @Operation(summary = "Delete recipe by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })

    /**s
      This method deletes recipe by given ID

      @param id the id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable @Valid long id) {
        recipeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Create a recipe")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Invalid recipe request",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    /**
      This method creates recipe with given request body

      @param recipeRequest the recipe request
     * @return the response entity
     */
    @PostMapping("/recipes")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        Recipe saved = recipeService.createRecipe(recipeRequest);
        return ResponseEntity.created(URI.create("/recipes/" + saved.getId())).body(saved);
    }

    @Operation(
            summary = "Update recipe details for which id and updated details"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Invalid update request",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })

    /**
     * This method updates recipe of given id with the information provided in
     * request body
     *
     * @param recipeRequest the recipe request
     * @param id            the id
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@Valid @RequestBody RecipeRequest recipeRequest,
                                                       @PathVariable long id) {
        Recipe updated =  recipeService.updateRecipe(id, recipeRequest);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = " filter recipes based on criteria"
    )
    @GetMapping(path = "/filteredRecipes", produces = "application/json")
    public ResponseEntity<List<RecipeDTO>> filterRecipe(
            @RequestParam(required = false) Boolean vegetarian,
            @RequestParam(required = false) Integer servings,
            @RequestParam(required = false, defaultValue = "") String include,
            @RequestParam(required = false, defaultValue = "") String exclude,
            @RequestParam(required = false) String instructionsContains,
            @RequestParam(required = false) String instructionsNotContains
    ) {
         SearchCriteria searchCriteria = new SearchCriteria(vegetarian, servings,
                new ArrayList<>(Arrays.asList(include.split(","))),
                new ArrayList<>(Arrays.asList(exclude.split(","))),
                instructionsContains, instructionsNotContains);

        List<RecipeDTO> result;
        if (searchCriteria.isEmpty()) {
            // No meaningful filters -> return all
            result = Optional.ofNullable(recipeService.getAll())
                    .orElseGet(List::of);
        } else {
            // Apply search only when criteria truly has active filters
            result = recipeService.findBySearchCriteria(searchCriteria).stream()
                    .sorted(Comparator.comparing(Recipe::getId))
                    .map(RecipeDTO::fromModel).toList();
        }

        return ResponseEntity.ok(result);
    }


}