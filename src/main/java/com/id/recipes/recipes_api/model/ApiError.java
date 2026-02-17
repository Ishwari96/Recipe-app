package com.id.recipes.recipes_api.model;


import java.time.Instant;

public record ApiError(
        String error,
        String message,
        String path,
        Instant timestamp
) {}
