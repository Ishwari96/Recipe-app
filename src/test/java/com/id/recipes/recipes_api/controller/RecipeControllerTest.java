package com.id.recipes.recipes_api.controller;


import com.id.recipes.recipes_api.model.RecipeDTO;
import com.id.recipes.recipes_api.model.RecipeEntity;
import com.id.recipes.recipes_api.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController controller;

    @Test
    void list_shouldReturnSortedRecipesAsDtoMap() {
        // Given: two entities out of order
        RecipeEntity r2 = new RecipeEntity();
        r2.setId(2);

        RecipeEntity r1 = new RecipeEntity();
        r1.setId(1);

        when(recipeService.findAll()).thenReturn(List.of(r2, r1));

        // Mock DTO mapping
        RecipeDTO dto1 = new RecipeDTO(1, "Recipe 1");
        RecipeDTO dto2 = new RecipeDTO(2, "Recipe 2");

        // Use Mockito to mock static call RecipeDTO.map
        try (var mocked = org.mockito.Mockito.mockStatic(RecipeDTO.class)) {
            mocked.when(() -> RecipeDTO.map(r1)).thenReturn(dto1);
            mocked.when(() -> RecipeDTO.map(r2)).thenReturn(dto2);

            // When
            Map<String, Object> result = controller.list();

            // Then
            assertThat(result).containsKey("recipes");

            @SuppressWarnings("unchecked")
            List<RecipeDTO> list = (List<RecipeDTO>) result.get("recipes");

            assertThat(list)
                    .hasSize(2)
                    .containsExactly(dto1, dto2); // sorted by ID asc
        }
    }
}
