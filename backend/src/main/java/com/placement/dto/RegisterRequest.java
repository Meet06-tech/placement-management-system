package com.placement.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 150, message = "Full name must be at most 150 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotBlank(message = "Enrollment number is required")
        @Size(max = 20, message = "Enrollment number must be at most 20 characters")
        String enrollmentNumber,

        @NotNull(message = "Branch ID is required")
        Long branchId,

        @NotNull(message = "Graduation year is required")
        Short graduationYear,

        @NotNull(message = "Active backlogs count is required")
        @Min(value = 0, message = "Active backlogs cannot be negative")
        Short activeBacklogs
) {}
