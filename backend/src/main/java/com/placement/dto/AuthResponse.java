package com.placement.dto;

public record AuthResponse(
        String token,
        Long userId,
        String email,
        String fullName,
        String role,
        Long entityId  // studentId for STUDENT, coordinatorId for COORDINATOR
) {}
