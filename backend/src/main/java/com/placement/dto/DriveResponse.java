package com.placement.dto;

import com.placement.entity.Company;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record DriveResponse(
        Long id,
        Company company,
        String jobRole,
        String jobDescription,
        BigDecimal minPackageLpa,
        BigDecimal maxPackageLpa,
        String location,
        String driveType,
        List<String> eligibleBranches,
        OffsetDateTime applicationDeadline,
        LocalDate driveDate,
        String status,
        OffsetDateTime createdAt
) {}
