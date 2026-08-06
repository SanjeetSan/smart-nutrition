package com.smartnutrition.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StudentResponse(
    Long id,
    String name,
    String rollNumber,
    String studentCode,
    String gender,
    LocalDate dateOfBirth,
    BigDecimal weightKg,
    BigDecimal heightCm,
    String bloodGroup,
    String className,
    String schoolName
) {}
