package com.id.recipes.recipes_api.model.rest;

import com.id.recipes.recipes_api.model.Ingredient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;


import java.util.List;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RecipeRequest {

	/** The name. */
	@Length(min = 3, max = 20)
	private String title;

	/** The is vegetarian. */
    private boolean vegetarian;

	/** The number of person. */
	@Max(value = 50)
	private int numberOfPerson;

	/** The cooking instruction. */
	@Length(min = 3, max = 100)
	private String cookingInstruction;

	/** The ingredients. */
	@Valid
	@NotEmpty
	@Size(min = 1)
	private List<Ingredient> ingredients;

}
