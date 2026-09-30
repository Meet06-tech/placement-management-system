package com.placement.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ApplicationResponse(
        Long id,
        Long studentId,
        String studentName,
        Long driveId,
        String companyName,
        String jobRole,
        String status,
        OffsetDateTime appliedAt,
        List<RoundResultDto> roundResults
) {
    public record RoundResultDto(
        Long roundId,
        Integer roundNumber,
        String roundName,
        String result,    // PENDING, SELECTED, REJECTED, ABSENT
        String remarks
    ) {}
}
