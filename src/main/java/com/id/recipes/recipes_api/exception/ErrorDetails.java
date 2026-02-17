package com.id.recipes.recipes_api.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorDetails {

    /** The timestamp. */
    private Date timestamp;

    /** The message. */
    private String message;

    /** The details. */
    private String details;
}