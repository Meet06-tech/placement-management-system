package com.placement.dto;

import com.placement.entity.DriveType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

public record DriveRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        String jobRole,
        String jobDescription,
        BigDecimal minPackageLpa,
        BigDecimal maxPackageLpa,
        String location,

        @NotNull(message = "Drive type is required")
        DriveType driveType,

        @NotNull(message = "Application deadline is required")
        OffsetDateTime applicationDeadline,

        LocalDate driveDate,
        Set<Long> eligibleBranchIds
) {}
