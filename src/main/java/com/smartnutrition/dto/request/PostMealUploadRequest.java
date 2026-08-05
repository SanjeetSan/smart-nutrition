package com.smartnutrition.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PostMealUploadRequest(
    @NotNull(message = "Meal ID is required")
    Long mealId,

    String postMealImageUrl,

    // Teacher remarks
    String teacherRemark,

    // Per-item consumption percentages (keyed by food item name)
    // If the teacher uses a simple overall slider instead of per-item
    @DecimalMin(value = "0.0") @DecimalMax(value = "100.0")
    BigDecimal overallConsumptionPercentage
) {}
