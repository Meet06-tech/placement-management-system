package com.placement.dto;

import java.math.BigDecimal;
import java.util.List;

public record StudentProfileResponse(
        Long id,
        String fullName,
        String email,
        String enrollmentNumber,
        String branchName,
        String branchCode,
        Long branchId,
        BigDecimal cgpa,
        String phone,
        Short activeBacklogs,
        Short currentSemester,
        Short graduationYear,
        String gender,
        String address,
        String resumeFileUrl,
        String profilePhotoUrl,
        List<SkillDto> skills
) {
    public record SkillDto(Long id, String name) {}
}
