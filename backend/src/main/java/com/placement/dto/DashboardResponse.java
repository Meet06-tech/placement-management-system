package com.placement.dto;

public record DashboardResponse() {

    public record StudentDashboard(
            long availableDrivesCount,
            long myApplicationsCount,
            long inProgressCount,
            long selectedCount
    ) {}

    public record CoordinatorDashboard(
            long totalStudents,
            long totalCompanies,
            long activeDrivesCount,
            long totalApplications,
            long selectedStudentsCount
    ) {}
}
