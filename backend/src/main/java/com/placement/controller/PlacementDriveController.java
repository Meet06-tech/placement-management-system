package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.DriveRequest;
import com.placement.dto.StudentProfileResponse;
import com.placement.entity.User;
import com.placement.repository.UserRepository;
import com.placement.service.PlacementDriveService;
import com.placement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drives")
@RequiredArgsConstructor
public class PlacementDriveController {

    private final PlacementDriveService driveService;
    private final StudentService studentService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).map(User::getId).orElse(null);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllDrives() {
        return ResponseEntity.ok(new ApiResponse(true, "Drives fetched", driveService.getAllDrives()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDrive(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Drive fetched", driveService.getDriveById(id)));
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> createDrive(@Valid @RequestBody DriveRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Drive created", driveService.createDrive(getCurrentUserId(), request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> updateDrive(@PathVariable Long id, @Valid @RequestBody DriveRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Drive updated", driveService.updateDrive(id, request)));
    }

    @GetMapping("/{id}/check-eligibility")
    public ResponseEntity<ApiResponse> checkEligibility(@PathVariable Long id) {
        StudentProfileResponse profile = studentService.getProfileByUserId(getCurrentUserId());
        boolean eligible = driveService.checkEligibility(profile.id(), id);
        return ResponseEntity.ok(new ApiResponse(true, "Eligibility checked", eligible));
    }
}
