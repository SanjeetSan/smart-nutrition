package com.smartnutrition.service;

import com.smartnutrition.dto.request.UpdateProfileRequest;
import com.smartnutrition.dto.response.UserResponse;
import com.smartnutrition.entity.User;
import com.smartnutrition.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse updateProfile(String currentEmail, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.email() != null && !request.email().equalsIgnoreCase(currentEmail)) {
            if (userRepository.findByEmail(request.email()).isPresent()) {
                throw new IllegalArgumentException("Email already in use by another account");
            }
            user.setEmail(request.email());
        }

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}
