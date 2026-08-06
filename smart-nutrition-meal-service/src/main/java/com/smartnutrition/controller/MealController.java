package com.smartnutrition.controller;

import com.smartnutrition.dto.request.PostMealUploadRequest;
import com.smartnutrition.dto.request.PreMealUploadRequest;
import com.smartnutrition.dto.response.MealResponse;
import com.smartnutrition.dto.response.NutritionScoreResponse;
import com.smartnutrition.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meals")
@Tag(name = "Meals", description = "Pre-meal and post-meal upload endpoints for parents and teachers")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("/pre-meal")
    @PreAuthorize("hasRole('PARENT')")
    @Operation(summary = "Parent uploads pre-meal image and manually enters food item details")
    public ResponseEntity<MealResponse> uploadPreMeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PreMealUploadRequest request) {
        MealResponse response = mealService.processPreMealUpload(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/post-meal")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Teacher uploads post-meal image, enters consumption %, triggers 35% Lunch RDA scoring")
    public ResponseEntity<NutritionScoreResponse> uploadPostMeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostMealUploadRequest request) {
        NutritionScoreResponse response = mealService.processPostMealUpload(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
