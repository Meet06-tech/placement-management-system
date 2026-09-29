package com.placement.service;

import com.placement.dto.DriveRequest;
import com.placement.dto.DriveResponse;
import com.placement.entity.*;
import com.placement.exception.BadRequestException;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlacementDriveService {

    private final PlacementDriveRepository driveRepository;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private final StudentRepository studentRepository;
    private final CoordinatorRepository coordinatorRepository;

    public List<DriveResponse> getAllDrives() {
        return driveRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DriveResponse> getPublishedDrives() {
        return driveRepository.findByStatus(DriveStatus.PUBLISHED).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DriveResponse getDriveById(Long id) {
        return driveRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found"));
    }

    @Transactional
    public DriveResponse createDrive(Long coordinatorUserId, DriveRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        Coordinator coordinator = coordinatorRepository.findById(coordinatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinator not found"));

        Set<Branch> eligibleBranches = Set.of();
        if (request.eligibleBranchIds() != null) {
            eligibleBranches = request.eligibleBranchIds().stream()
                    .map(id -> branchRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + id)))
                    .collect(Collectors.toSet());
        }

        PlacementDrive drive = PlacementDrive.builder()
                .company(company)
                .jobRole(request.jobRole())
                .jobDescription(request.jobDescription())
                .minPackageLpa(request.minPackageLpa())
                .maxPackageLpa(request.maxPackageLpa())
                .location(request.location())
                .driveType(request.driveType())
                .applicationDeadline(request.applicationDeadline())
                .driveDate(request.driveDate())
                .eligibleBranches(eligibleBranches)
                .createdBy(coordinator)
                .status(DriveStatus.DRAFT)
                .build();

        return mapToResponse(driveRepository.save(drive));
    }

    @Transactional
    public DriveResponse updateDrive(Long id, DriveRequest request) {
        PlacementDrive drive = driveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found"));

        if (request.companyId() != null) {
            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
            drive.setCompany(company);
        }
        if (request.jobRole() != null) drive.setJobRole(request.jobRole());
        if (request.jobDescription() != null) drive.setJobDescription(request.jobDescription());
        if (request.minPackageLpa() != null) drive.setMinPackageLpa(request.minPackageLpa());
        if (request.maxPackageLpa() != null) drive.setMaxPackageLpa(request.maxPackageLpa());
        if (request.location() != null) drive.setLocation(request.location());
        if (request.driveType() != null) drive.setDriveType(request.driveType());
        if (request.applicationDeadline() != null) drive.setApplicationDeadline(request.applicationDeadline());
        if (request.driveDate() != null) drive.setDriveDate(request.driveDate());
        if (request.eligibleBranchIds() != null) {
            Set<Branch> branches = request.eligibleBranchIds().stream()
                    .map(bId -> branchRepository.findById(bId)
                            .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + bId)))
                    .collect(Collectors.toSet());
            drive.setEligibleBranches(branches);
        }

        return mapToResponse(driveRepository.save(drive));
    }

    /**
     * Check eligibility based on EligibilityRule linked to the drive.
     */
    public boolean checkEligibility(Long studentId, Long driveId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found"));

        if (drive.getStatus() != DriveStatus.PUBLISHED) return false;
        if (drive.getApplicationDeadline() != null && OffsetDateTime.now().isAfter(drive.getApplicationDeadline())) return false;
        if (!drive.getEligibleBranches().contains(student.getBranch())) return false;

        EligibilityRule rule = drive.getEligibilityRule();
        if (rule != null) {
            if (rule.getMinCgpa() != null && student.getCgpa() != null
                    && student.getCgpa().compareTo(rule.getMinCgpa()) < 0) return false;
            if (rule.getMaxBacklogs() != null
                    && student.getActiveBacklogs() > rule.getMaxBacklogs()) return false;
            if (rule.getMinGraduationYear() != null
                    && student.getGraduationYear() < rule.getMinGraduationYear()) return false;
            if (rule.getMaxGraduationYear() != null
                    && student.getGraduationYear() > rule.getMaxGraduationYear()) return false;
        }

        return true;
    }

    private DriveResponse mapToResponse(PlacementDrive drive) {
        List<String> branchNames = drive.getEligibleBranches().stream()
                .map(Branch::getName)
                .collect(Collectors.toList());

        return new DriveResponse(
                drive.getId(),
                drive.getCompany(),
                drive.getJobRole(),
                drive.getJobDescription(),
                drive.getMinPackageLpa(),
                drive.getMaxPackageLpa(),
                drive.getLocation(),
                drive.getDriveType() != null ? drive.getDriveType().name() : null,
                branchNames,
                drive.getApplicationDeadline(),
                drive.getDriveDate(),
                drive.getStatus().name(),
                drive.getCreatedAt()
        );
    }
}
