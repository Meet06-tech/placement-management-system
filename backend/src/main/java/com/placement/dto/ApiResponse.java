package com.placement.dto;

public record ApiResponse(
        boolean success,
        String message,
        Object data
) {}
