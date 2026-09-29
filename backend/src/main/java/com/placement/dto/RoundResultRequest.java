package com.placement.dto;

import jakarta.validation.constraints.NotNull;

public record RoundResultRequest(
        @NotNull(message = "Application ID is required")
        Long applicationId,
        
        @NotNull(message = "Status is required")
        String status,
        
        String remarks
) {}
