package com.smartnutrition.repository;

import com.smartnutrition.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Optional<Meal> findByStudentIdAndMealDate(Long studentId, LocalDate mealDate);
}
