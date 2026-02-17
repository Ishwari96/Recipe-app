package com.id.recipes.recipes_api.model.rest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;



/**
 * Instantiates a new ingredient request.
 */
@Data
public class IngredientRequest {

    private long id;

    /** The ingredient name. */
    @Length(min = 3, max = 20)
    private String ingredientName;

    /** The ingredient desciption. */
    @Length(min = 3, max = 100)
    private String ingredientDesciption;

}
