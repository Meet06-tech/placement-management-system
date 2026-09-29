package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.CompanyRequest;
import com.placement.entity.User;
import com.placement.repository.UserRepository;
import com.placement.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).map(User::getId).orElse(null);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllCompanies() {
        return ResponseEntity.ok(new ApiResponse(true, "Companies fetched", companyService.getAllCompanies()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Company fetched", companyService.getCompanyById(id)));
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> createCompany(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Company created",
                companyService.createCompany(getCurrentUserId(), request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Company updated", companyService.updateCompany(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(new ApiResponse(true, "Company deleted", null));
    }
}
