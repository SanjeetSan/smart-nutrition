package com.smartnutrition.dto.request;

import com.smartnutrition.enums.FoodItemSource;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record FoodItemDto(
    @NotBlank(message = "Food name is required")
    String foodName,

    String quantity,       // e.g. "150g" or "1 cup"
    String cookingNote,    // Manual recipe note from parent
    BigDecimal calories,
    BigDecimal proteinG,
    BigDecimal carbsG,
    BigDecimal fatG,
    BigDecimal fiberG,
    FoodItemSource source
) {}
