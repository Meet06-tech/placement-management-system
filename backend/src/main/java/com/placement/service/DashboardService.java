package com.placement.service;

import com.placement.dto.DashboardResponse;
import com.placement.entity.ApplicationStatus;
import com.placement.entity.DriveStatus;
import com.placement.entity.Student;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.ApplicationRepository;
import com.placement.repository.CompanyRepository;
import com.placement.repository.PlacementDriveRepository;
import com.placement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final PlacementDriveRepository driveRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;

    public DashboardResponse.StudentDashboard getStudentDashboard(Long userId) {
        // With shared-PK, userId == studentId
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // PUBLISHED drives whose eligible branches include this student's branch
        long availableDrives = driveRepository.findByStatus(DriveStatus.PUBLISHED).stream()
                .filter(drive -> drive.getEligibleBranches().contains(student.getBranch()))
                .count();

        long myApplications = applicationRepository.findByStudentId(student.getId()).size();

        long inProgressCount = applicationRepository.findByStudentId(student.getId()).stream()
                .filter(a -> a.getStatus() == ApplicationStatus.IN_PROGRESS)
                .count();

        long selectedCount = applicationRepository.findByStudentId(student.getId()).stream()
                .filter(a -> a.getStatus() == ApplicationStatus.SELECTED)
                .count();

        return new DashboardResponse.StudentDashboard(
                availableDrives,
                myApplications,
                inProgressCount,
                selectedCount
        );
    }

    public DashboardResponse.CoordinatorDashboard getCoordinatorDashboard() {
        long totalStudents = studentRepository.count();
        long totalCompanies = companyRepository.count();
        long activeDrivesCount = driveRepository.findByStatus(DriveStatus.PUBLISHED).size();
        long totalApplications = applicationRepository.count();
        long selectedStudentsCount = applicationRepository.countByStatus(ApplicationStatus.SELECTED);

        return new DashboardResponse.CoordinatorDashboard(
                totalStudents,
                totalCompanies,
                activeDrivesCount,
                totalApplications,
                selectedStudentsCount
        );
    }
}
