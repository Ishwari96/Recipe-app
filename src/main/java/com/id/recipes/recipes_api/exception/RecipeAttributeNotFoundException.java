package com.id.recipes.recipes_api.exception;

/**
 * The Class RecipeAttributeNotFoundException.
 */
public class RecipeAttributeNotFoundException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7929854473093802106L;

    /**
     * Instantiates a new recipe attribute not found exception.
     *
     * @param message the message
     */
    public RecipeAttributeNotFoundException(String message) {
        super(message);
    }
}
