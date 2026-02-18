package com.id.recipes.recipes_api.service;

import com.id.recipes.recipes_api.model.Ingredient;
import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.model.dto.RecipeDTO;
import com.id.recipes.recipes_api.model.rest.RecipeRequest;
import com.id.recipes.recipes_api.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    private List<Recipe> recipes;
    private RecipeRequest recipeRequests;
    private Recipe recipe;
    List<Ingredient> ingredientEntities;
    Set<Ingredient> ingredients;
    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setId(1);
        recipe.setTitle("test");
        recipe.setInstructions("oven");
        recipe.setServings(4);
        recipe.setVegetarian(true);
        ingredients = new HashSet<>();

        recipeRequests = new RecipeRequest();
        recipeRequests.setCookingInstruction("oven");
        recipeRequests.setTitle("pasta");
        recipeRequests.setNumberOfPerson(4);
        recipeRequests.setVegetarian(true);
        ingredientEntities = new ArrayList<>();
        Ingredient ingredientEntity1 = new Ingredient();
        ingredientEntity1.setId(1);
        ingredientEntity1.setName("tomato");
        ingredientEntity1.setDescription("red");
        Ingredient ingredientEntity2 = new Ingredient();
        ingredientEntity2.setId(2);
        ingredientEntity2.setName("olive");
        ingredientEntity2.setDescription("green");
        ingredientEntities.add(ingredientEntity1);
        ingredientEntities.add(ingredientEntity2);
        recipeRequests.setIngredients(ingredientEntities);
        ingredients.add(ingredientEntity1);
        recipe.setIngredients(ingredients);
        recipes = new ArrayList<>();
        recipes.add(recipe);
    }

    @Test
    void whenGetAll_thenReturnRecipeList() {
        when(recipeRepository.findAll()).thenReturn(recipes);

        List<RecipeDTO> result = recipeService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().id());
    }

    @Test
    void whenFindById_thenReturnRecipe() {
        when(recipeRepository.findById(1L))
                .thenReturn(Optional.of(recipe));

        Optional<RecipeDTO> result = Optional.ofNullable(recipeService.findById(1L));

        assertTrue(result.isPresent());
        assertEquals(1, result.get().id());
    }

    @Test
    void whenCreateRecipe_thenReturnRecipe() {

        // Stub repository.save
        when(recipeRepository.save(any(Recipe.class)))
                .thenAnswer(invocation -> {
                    Recipe toSave = invocation.getArgument(0);
                    toSave.setId(1L);
                    return toSave;
                });

        // Act
        Recipe result = recipeService.createRecipe(recipeRequests);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("pasta", result.getTitle());
        assertTrue(result.isVegetarian());
        assertEquals(4, result.getServings());

        verify(recipeRepository, times(1)).save(any(Recipe.class));

    }
    @Test
    void whenUpdateRecipe_thenReturnUpdatedRecipe() {
        long id = 1L;
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setIngredients(ingredientEntities);
        recipeRequest.setTitle("curry");
        recipeRequest.setCookingInstruction("airfryer");
        // Mock: recipe exists
        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));

        // Mock: save returns the updated recipe
        when(recipeRepository.save(recipe))
                .thenReturn(recipe);

        Recipe result = recipeService.updateRecipe(id,recipeRequest);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void whenDeleteRecipe_thenDeleteIsCalled() {
        long id = 1L;

        Mockito.doNothing().when(recipeRepository).deleteById(id);

        recipeService.deleteById(id);

        verify(recipeRepository, times(1)).deleteById(id);
    }



}