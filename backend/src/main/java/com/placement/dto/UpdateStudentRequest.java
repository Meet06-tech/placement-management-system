package com.placement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateStudentRequest(
        String fullName,
        String phone,
        BigDecimal cgpa,
        Long branchId,
        Short activeBacklogs,
        Short currentSemester,
        LocalDate dateOfBirth,
        String gender,
        String address
) {}
