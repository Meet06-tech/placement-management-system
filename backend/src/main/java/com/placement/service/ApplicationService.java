package com.placement.service;

import com.placement.dto.ApplicationResponse;
import com.placement.entity.*;
import com.placement.exception.BadRequestException;
import com.placement.exception.DuplicateResourceException;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final PlacementDriveRepository driveRepository;
    private final RoundResultRepository roundResultRepository;
    private final PlacementDriveService placementDriveService;

    @Transactional
    public ApplicationResponse apply(Long studentId, Long driveId) {
        // Business rule: student cannot apply to same drive twice
        if (applicationRepository.findByStudentIdAndPlacementDriveId(studentId, driveId).isPresent()) {
            throw new DuplicateResourceException("Already applied to this drive");
        }

        if (!placementDriveService.checkEligibility(studentId, driveId)) {
            throw new BadRequestException("Student is not eligible for this drive");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found"));

        Application application = Application.builder()
                .student(student)
                .placementDrive(drive)
                .status(ApplicationStatus.APPLIED)
                .build();

        return mapToResponse(applicationRepository.save(application));
    }

    public List<ApplicationResponse> getStudentApplications(Long studentId) {
        return applicationRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> getDriveApplicants(Long driveId) {
        return applicationRepository.findByPlacementDriveId(driveId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        application.setStatus(status);
        return mapToResponse(applicationRepository.save(application));
    }

    private ApplicationResponse mapToResponse(Application application) {
        List<ApplicationResponse.RoundResultDto> results = roundResultRepository
                .findByApplicationId(application.getId()).stream()
                .map(r -> new ApplicationResponse.RoundResultDto(
                        r.getId(),
                        r.getPlacementRound().getRoundNumber().intValue(),
                        r.getPlacementRound().getRoundName(),
                        r.getResult().name(),          // field is 'result' not 'status'
                        r.getRemarks()
                ))
                .collect(Collectors.toList());

        String studentName = application.getStudent().getFullName();  // in Student, not User

        return new ApplicationResponse(
                application.getId(),
                application.getStudent().getId(),
                studentName,
                application.getPlacementDrive().getId(),
                application.getPlacementDrive().getCompany().getName(),
                application.getPlacementDrive().getJobRole(),
                application.getStatus().name(),
                application.getAppliedAt(),
                results
        );
    }
}
