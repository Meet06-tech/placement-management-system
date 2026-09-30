package com.placement.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        String name,

        String description,
        String website,
        String industry,
        String hrContactName,
        String hrContactEmail,
        String hrContactPhone
) {}
