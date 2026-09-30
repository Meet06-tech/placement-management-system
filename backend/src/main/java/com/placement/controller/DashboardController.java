package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.entity.User;
import com.placement.repository.UserRepository;
import com.placement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).map(User::getId).orElse(null);
    }

    @GetMapping("/student")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse> getStudentDashboard() {
        return ResponseEntity.ok(new ApiResponse(true, "Dashboard fetched", dashboardService.getStudentDashboard(getCurrentUserId())));
    }

    @GetMapping("/coordinator")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> getCoordinatorDashboard() {
        return ResponseEntity.ok(new ApiResponse(true, "Dashboard fetched", dashboardService.getCoordinatorDashboard()));
    }
}
