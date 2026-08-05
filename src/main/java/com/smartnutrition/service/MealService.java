package com.smartnutrition.service;

import com.smartnutrition.dto.request.FoodItemDto;
import com.smartnutrition.dto.request.PostMealUploadRequest;
import com.smartnutrition.dto.request.PreMealUploadRequest;
import com.smartnutrition.dto.response.MealResponse;
import com.smartnutrition.dto.response.NutritionScoreResponse;
import com.smartnutrition.entity.Meal;
import com.smartnutrition.entity.MealFoodItem;
import com.smartnutrition.entity.NutritionScore;
import com.smartnutrition.entity.Student;
import com.smartnutrition.entity.User;
import com.smartnutrition.enums.FoodItemSource;
import com.smartnutrition.enums.MealStatus;
import com.smartnutrition.repository.MealFoodItemRepository;
import com.smartnutrition.repository.MealRepository;
import com.smartnutrition.repository.StudentRepository;
import com.smartnutrition.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final MealFoodItemRepository mealFoodItemRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final NutritionScoringService nutritionScoringService;

    public MealService(MealRepository mealRepository,
                       MealFoodItemRepository mealFoodItemRepository,
                       StudentRepository studentRepository,
                       UserRepository userRepository,
                       NutritionScoringService nutritionScoringService) {
        this.mealRepository = mealRepository;
        this.mealFoodItemRepository = mealFoodItemRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.nutritionScoringService = nutritionScoringService;
    }

    @Transactional
    public MealResponse processPreMealUpload(String parentEmail, PreMealUploadRequest request) {
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Parent user not found"));

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        LocalDate today = LocalDate.now();

        Meal meal = mealRepository.findByStudentIdAndMealDate(student.getId(), today)
                .orElseGet(() -> Meal.builder()
                        .student(student)
                        .mealDate(today)
                        .uploadedByParent(parent)
                        .status(MealStatus.PRE_MEAL_UPLOADED)
                        .build());

        meal.setPreMealImageUrl(request.preMealImageUrl());
        meal.setStatus(MealStatus.PRE_MEAL_UPLOADED);

        Meal savedMeal = mealRepository.save(meal);

        List<MealFoodItem> existing = mealFoodItemRepository.findByMealId(savedMeal.getId());
        mealFoodItemRepository.deleteAll(existing);

        List<MealFoodItem> foodItemEntities = new ArrayList<>();

        for (FoodItemDto itemDto : request.foodItems()) {
            MealFoodItem item = MealFoodItem.builder()
                    .meal(savedMeal)
                    .foodName(itemDto.foodName())
                    .quantity(itemDto.quantity() != null ? itemDto.quantity() : "1 serving")
                    .cookingNote(itemDto.cookingNote())
                    .calories(itemDto.calories() != null ? itemDto.calories() : new BigDecimal("250.00"))
                    .proteinG(itemDto.proteinG() != null ? itemDto.proteinG() : new BigDecimal("8.00"))
                    .carbsG(itemDto.carbsG() != null ? itemDto.carbsG() : new BigDecimal("35.00"))
                    .fatG(itemDto.fatG() != null ? itemDto.fatG() : new BigDecimal("6.00"))
                    .fiberG(itemDto.fiberG() != null ? itemDto.fiberG() : new BigDecimal("4.00"))
                    .source(itemDto.source() != null ? itemDto.source() : FoodItemSource.PARENT_EDITED)
                    .build();
            foodItemEntities.add(item);
        }

        List<MealFoodItem> savedItems = mealFoodItemRepository.saveAll(foodItemEntities);
        return mapToMealResponse(savedMeal, savedItems);
    }

    @Transactional
    public NutritionScoreResponse processPostMealUpload(String teacherEmail, PostMealUploadRequest request) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new IllegalArgumentException("Teacher user not found"));

        Meal meal = mealRepository.findById(request.mealId())
                .orElseThrow(() -> new IllegalArgumentException("Meal not found with ID: " + request.mealId()));

        meal.setPostMealImageUrl(request.postMealImageUrl());
        meal.setStatus(MealStatus.POST_MEAL_UPLOADED);
        meal.setUploadedByTeacher(teacher);
        mealRepository.save(meal);

        List<MealFoodItem> items = mealFoodItemRepository.findByMealId(meal.getId());

        BigDecimal consumptionRatio = (request.overallConsumptionPercentage() != null)
                ? request.overallConsumptionPercentage().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)
                : new BigDecimal("1.00");

        for (MealFoodItem item : items) {
            item.setConsumptionPercentage(consumptionRatio.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
            item.setConsumedCalories(item.getCalories() != null ? item.getCalories().multiply(consumptionRatio).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            item.setConsumedProteinG(item.getProteinG() != null ? item.getProteinG().multiply(consumptionRatio).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            item.setConsumedCarbsG(item.getCarbsG() != null ? item.getCarbsG().multiply(consumptionRatio).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            item.setConsumedFatG(item.getFatG() != null ? item.getFatG().multiply(consumptionRatio).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            item.setConsumedFiberG(item.getFiberG() != null ? item.getFiberG().multiply(consumptionRatio).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        }
        mealFoodItemRepository.saveAll(items);

        NutritionScore score = nutritionScoringService.calculateAndSaveScore(meal.getStudent(), meal, items);
        return mapToScoreResponse(score);
    }

    private NutritionScoreResponse mapToScoreResponse(NutritionScore score) {
        return new NutritionScoreResponse(
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
        );
    }

    private MealResponse mapToMealResponse(Meal meal, List<MealFoodItem> items) {
        List<FoodItemDto> itemDtos = items.stream().map(i -> new FoodItemDto(
                i.getFoodName(),
                i.getQuantity(),
                i.getCookingNote(),
                i.getCalories(),
                i.getProteinG(),
                i.getCarbsG(),
                i.getFatG(),
                i.getFiberG(),
                i.getSource()
        )).toList();

        return new MealResponse(
                meal.getId(),
                meal.getStudent().getId(),
                meal.getMealDate(),
                meal.getPreMealImageUrl(),
                meal.getPostMealImageUrl(),
                meal.getStatus(),
                itemDtos
        );
    }
}
