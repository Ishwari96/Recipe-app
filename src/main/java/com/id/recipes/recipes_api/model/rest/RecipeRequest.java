package com.id.recipes.recipes_api.model.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data
@NoArgsConstructor
public class RecipeRequest {

	private long id;

	/** The name. */
	@Length(min = 3, max = 20)
	private String name;

	/** The is vegetarian. */
	private boolean isVegetarian;

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
	private List<IngredientRequest> ingredients;

}
