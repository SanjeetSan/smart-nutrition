package com.smartnutrition.controller;

import com.smartnutrition.dto.response.NutritionScoreResponse;
import com.smartnutrition.entity.NutritionScore;
import com.smartnutrition.repository.NutritionScoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Nutrition score reports and 7-day weekly trend endpoints")
public class ReportController {

    private final NutritionScoreRepository nutritionScoreRepository;

    public ReportController(NutritionScoreRepository nutritionScoreRepository) {
        this.nutritionScoreRepository = nutritionScoreRepository;
    }

    @GetMapping("/weekly/{studentId}")
    @PreAuthorize("hasAnyRole('PARENT', 'TEACHER')")
    @Operation(summary = "Get last 7 days nutrition scores for a student (for trend chart on dashboard)")
    public ResponseEntity<List<NutritionScoreResponse>> getWeeklyReport(@PathVariable Long studentId) {
        List<NutritionScore> scores = nutritionScoreRepository.findTop7ByStudentIdOrderByCalculatedAtDesc(studentId);
        List<NutritionScoreResponse> response = scores.stream().map(score -> new NutritionScoreResponse(
                score.getId(),
                score.getStudent().getId(),
                score.getMeal().getId(),
                score.getScore(),
                score.getClassification(),
                score.getTotalConsumedCalories(),
                score.getTotalConsumedProteinG(),
                score.getTotalConsumedCarbsG(),
                score.getTotalConsumedFatG(),
                score.getTotalConsumedFiberG(),
                score.getLunchCalorieTarget(),
                score.getLunchProteinTarget(),
                score.getCalculatedAt() != null ? score.getCalculatedAt().toString() : null
        )).toList();
        return ResponseEntity.ok(response);
    }
}
