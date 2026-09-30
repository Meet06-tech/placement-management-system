package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final StudentService studentService;

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse> getAllSkills() {
        return ResponseEntity.ok(new ApiResponse(true, "All skills", studentService.getAllSkills()));
    }

    @GetMapping("/branches")
    public ResponseEntity<ApiResponse> getAllBranches() {
        return ResponseEntity.ok(new ApiResponse(true, "All branches", studentService.getAllBranches()));
    }
}
