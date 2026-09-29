package com.placement.dto;

import com.placement.entity.RoundType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoundRequest(
        @NotBlank(message = "Round name is required")
        String roundName,

        @NotNull(message = "Round type is required")
        RoundType roundType,

        String description
) {}
