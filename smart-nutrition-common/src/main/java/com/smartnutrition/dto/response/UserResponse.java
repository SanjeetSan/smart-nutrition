package com.smartnutrition.dto.response;

import com.smartnutrition.enums.Role;

public record UserResponse(
    Long id,
    String name,
    String email,
    Role role
) {}
