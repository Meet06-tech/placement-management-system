package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.StudentProfileResponse;
import com.placement.entity.ApplicationStatus;
import com.placement.entity.User;
import com.placement.repository.UserRepository;
import com.placement.service.ApplicationService;
import com.placement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final StudentService studentService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).map(User::getId).orElse(null);
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse> apply(@RequestBody Map<String, Long> payload) {
        Long driveId = payload.get("driveId");
        StudentProfileResponse profile = studentService.getProfileByUserId(getCurrentUserId());
        return ResponseEntity.ok(new ApiResponse(true, "Applied successfully", applicationService.apply(profile.id(), driveId)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse> getMyApplications() {
        StudentProfileResponse profile = studentService.getProfileByUserId(getCurrentUserId());
        return ResponseEntity.ok(new ApiResponse(true, "Applications fetched", applicationService.getStudentApplications(profile.id())));
    }

    @GetMapping("/drive/{driveId}")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> getDriveApplicants(@PathVariable Long driveId) {
        return ResponseEntity.ok(new ApiResponse(true, "Applicants fetched", applicationService.getDriveApplicants(driveId)));
    }

    @PutMapping("/{id}/shortlist")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> shortlistApplicant(@PathVariable Long id) {
        // SHORTLISTED removed from schema — move to IN_PROGRESS
        return ResponseEntity.ok(new ApiResponse(true, "Application moved to in-progress",
                applicationService.updateApplicationStatus(id, ApplicationStatus.IN_PROGRESS)));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        ApplicationStatus status = ApplicationStatus.valueOf(payload.get("status"));
        return ResponseEntity.ok(new ApiResponse(true, "Status updated", applicationService.updateApplicationStatus(id, status)));
    }
}
