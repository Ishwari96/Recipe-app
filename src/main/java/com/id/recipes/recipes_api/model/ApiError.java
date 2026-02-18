package com.id.recipes.recipes_api.model;


import java.time.Instant;

public record ApiError(
        String details,
        String message,
        Instant timestamp
) {}
