package com.id.recipes.recipes_api.exception;

/**
 * The Class RecipeNotFoundException.
 */
public class RecipeNotFoundException extends RuntimeException {

    /**
     * Instantiates a new recipe not found exception.
     *
     * @param message the message
     */
    public RecipeNotFoundException(String message) {
        super(message);
    }

}
