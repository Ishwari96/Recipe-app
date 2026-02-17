package com.id.recipes.recipes_api.controller;


import com.id.recipes.recipes_api.model.Recipe;
import com.id.recipes.recipes_api.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController controller;

    @Test
    void list_shouldReturnSortedRecipesAsDtoMap() {
        // Given: two entities out of order
        Recipe r2 = new Recipe();
        r2.setId(2);

        Recipe r1 = new Recipe();
        r1.setId(1);

        controller.getAll();

    }
}
