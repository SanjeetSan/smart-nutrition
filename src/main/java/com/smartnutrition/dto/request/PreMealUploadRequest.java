package com.smartnutrition.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PreMealUploadRequest(
    @NotNull(message = "Student ID is required")
    Long studentId,

    String preMealImageUrl,

    @NotEmpty(message = "Food item list cannot be empty")
    List<FoodItemDto> foodItems
) {}
