package com.smartnutrition.service;

import com.smartnutrition.dto.request.UpdateUserRoleRequest;
import com.smartnutrition.dto.response.AdminUserResponse;
import com.smartnutrition.entity.User;
import com.smartnutrition.enums.Role;
import com.smartnutrition.repository.MealRepository;
import com.smartnutrition.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    public AdminService(UserRepository userRepository, MealRepository mealRepository) {
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new AdminUserResponse(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getIsActive(),
                        u.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public AdminUserResponse updateUserRole(Long userId, UpdateUserRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.setRole(request.role());
        User updated = userRepository.save(user);

        return new AdminUserResponse(
                updated.getId(),
                updated.getName(),
                updated.getEmail(),
                updated.getRole(),
                updated.getIsActive(),
                updated.getCreatedAt()
        );
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        user.setIsActive(false);
        userRepository.save(user);
    }

    public Map<String, Object> getSystemHealth() {
        long totalUsers = userRepository.count();
        long totalMeals = mealRepository.count();

        return Map.of(
                "status", "UP",
                "database", "MySQL (smart_nutrition_db)",
                "totalRegisteredUsers", totalUsers,
                "totalLoggedMeals", totalMeals,
                "environment", "Development / School Admin Operations"
        );
    }
}
