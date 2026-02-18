package com.id.recipes.recipes_api.controller;


import com.id.recipes.recipes_api.exception.GlobalExceptionHandler;
import com.id.recipes.recipes_api.exception.RecipeNotFoundException;
import com.id.recipes.recipes_api.model.Ingredient;
import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.model.dto.IngredientDTO;
import com.id.recipes.recipes_api.model.dto.RecipeDTO;
import com.id.recipes.recipes_api.model.rest.RecipeRequest;
import com.id.recipes.recipes_api.service.RecipeService;
import com.id.recipes.recipes_api.utility.RecipeSpecification;
import com.id.recipes.recipes_api.utility.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    private MockMvc mockMvc;
    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;
    private final ObjectMapper objectMapper = new ObjectMapper();
    Set<IngredientDTO> ingredients = new HashSet<>();

    @BeforeEach
    void setup(){
        //include GlobalExceptionHandler to allow controller advice handling (404 -> JSON)
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

    }

    @Test
    void testCreateRecipe() throws Exception {
        // Build request body
        RecipeRequest req = new RecipeRequest();
        req.setTitle("Paneer");
        req.setVegetarian(true);
        req.setNumberOfPerson(2);
        req.setCookingInstruction("oven");
        Ingredient ing = new Ingredient();
        ing.setName("Tomato");
        ing.setDescription("red");
        req.setIngredients(java.util.List.of(ing));

        // Returned entity from service
        Recipe saved = new Recipe();
        saved.setId(1L);
        saved.setTitle("Paneer");
        saved.setVegetarian(true);
        saved.setServings(2);
        saved.setInstructions("oven");
        saved.setIngredients(java.util.Set.copyOf(java.util.List.of(ing)));

        when(recipeService.createRecipe(any(RecipeRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/recipes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/recipes/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Paneer"))
                .andExpect(jsonPath("$.vegetarian").value(true))
                .andExpect(jsonPath("$.servings").value(2))
                .andExpect(jsonPath("$.instructions").value("oven"));

        verify(recipeService).createRecipe(any(RecipeRequest.class));
    }

    @Test
    void getFindById()  throws Exception {
        IngredientDTO ingredient = new IngredientDTO(1,"almond","nuts");
        Set<IngredientDTO> ingredients = new HashSet<>();
        ingredients.add(ingredient);
        RecipeDTO recipe = new RecipeDTO(1L, "milkshake", true, 2, ingredients, "mixer");
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/api/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("milkshake"));
    }

    @Test
    void getById_notFound()  throws Exception {

        long id = 999L;

        // service throws exception for missing recipe
        when(recipeService.findById(id))
                .thenThrow(new RecipeNotFoundException("Recipe not found"));

        mockMvc.perform(get("/api/v1/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
    @Test
    void testDeleteRecipeById() throws Exception {
        long id = 11;

        doNothing().when(recipeService).deleteById(id);

        mockMvc.perform(delete("/api/v1/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void filterRecipe_noFilters_callsGetAll_andReturnsDtos() throws Exception {
        // Arrange: service returns DTOs directly (empty criteria path)
        List<RecipeDTO> allDtos = List.of(
                new RecipeDTO(1L, "Aloo", true, 2, ingredients, "cook"),
                new RecipeDTO(2L, "Paneer", true, 4, ingredients, "cook2")
        );
        when(recipeService.getAll()).thenReturn(allDtos);

        // Act + Assert
        mockMvc.perform(get("/api/v1/filteredRecipes") // or "/filteredRecipes" if no class-level prefix
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Aloo"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Paneer"));

        // getAll() must be called; findBySearchCriteria must NOT be called in this path
        verify(recipeService, times(1)).getAll();
        verify(recipeService, never()).findBySearchCriteria(any());
    }

    /**
     * Scenario 2: With active filters -> calls findBySearchCriteria, sorts by Recipe::getId, then maps via DTO::fromModel
     * We pass vegetarian=true (meaningful) + servings=4 to activate the filtered path.
     * The service returns ENTITIES (Recipe) as per your controller code.
     * Controller should sort by entity id [even if service returns unsorted] and map to DTOs.
     */
    void filterRecipe_withFilters_callsFindByCriteria_sortsById_andMapsToDto() throws Exception {
        // Arrange: build some Recipes out of order by id
        Recipe r2 = new Recipe();
        r2.setId(2L);
        r2.setTitle("Second");
        r2.setVegetarian(true);
        r2.setServings(4);
        r2.setInstructions("cook2");

        Recipe r1 = new Recipe();
        r1.setId(1L);
        r1.setTitle("First");
        r1.setVegetarian(true);
        r1.setServings(4);
        r1.setInstructions("cook1");

        // Service returns a list of entities in reverse order to ensure controller sorts by id
        when(recipeService.findBySearchCriteria(any())).thenReturn(List.of(r2, r1));

        // We also want to assert that getAll() is not called in this path
        when(recipeService.getAll()).thenReturn(List.of()); // will not be used

        // Act + Assert
        mockMvc.perform(get("/api/v1/filteredRecipes") // or "/filteredRecipes"
                        .param("vegetarian", "true")            // meaningful
                        .param("servings", "4")                 // meaningful
                        .param("include", "tomato,onion")       // optional
                        .param("exclude", "peanut")             // optional
                        .param("instructionsContains", "cook")  // optional
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Expect sorted by id ascending: r1 first, then r2
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("First"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Second"));

        // Verify the right service call happened
        verify(recipeService, times(1)).findBySearchCriteria(any());
        verify(recipeService, never()).getAll();

        // (Optional) Capture SearchCriteria to assert it was built as expected
        ArgumentCaptor<SearchCriteria> captor =
                ArgumentCaptor.forClass(SearchCriteria.class);
        verify(recipeService).findBySearchCriteria(captor.capture());
        var crit = captor.getValue();

        // Basic sanity checks on captured criteria
        // vegetarian=true, servings=4, include/exclude split by comma
        // Instructions contains="cook" (as passed), instructionsNotContains should be null
        // NOTE: These assertions assume your SearchCriteria carries the raw values (not normalized to null/empty).
        org.junit.jupiter.api.Assertions.assertEquals(Boolean.TRUE, crit.vegetarian());
        org.junit.jupiter.api.Assertions.assertEquals(4, crit.servings());
        org.junit.jupiter.api.Assertions.assertEquals(List.of("tomato", "onion"), crit.include());
        org.junit.jupiter.api.Assertions.assertEquals(List.of("peanut"), crit.exclude());
        org.junit.jupiter.api.Assertions.assertEquals("cook", crit.instructionsContains());
        org.junit.jupiter.api.Assertions.assertNull(crit.instructionsNotContains());
    }

    /**
     * Scenario 3 (optional): include/exclude present but blank -> still considered empty filter if isEmpty() treats blanks properly.
     * This ensures that defaultValue="" does not accidentally activate filters (depends on your SearchCriteria.isEmpty implementation).
     */
    @Test
    void filterRecipe_blankIncludeExclude_shouldStillFallBackToGetAll_ifCriteriaEmpty() throws Exception {


        List<RecipeDTO> allDtos = List.of(new RecipeDTO(10L, "OnlyOne", false, 1, ingredients, "noop"));
        when(recipeService.getAll()).thenReturn(allDtos);

        mockMvc.perform(get("/api/v1/filteredRecipes")
                        .param("include", "")   // default blank
                        .param("exclude", "")   // default blank
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].title").value("OnlyOne"));

        verify(recipeService, times(1)).getAll();
        verify(recipeService, never()).findBySearchCriteria(any());
    }


    @Test
    void list_shouldReturnSortedRecipesAsDtoMap() {
        // Given: two entities out of order
        Recipe r2 = new Recipe();
        r2.setId(2);

        Recipe r1 = new Recipe();
        r1.setId(1);

        recipeController.getRecipes();

    }
}
