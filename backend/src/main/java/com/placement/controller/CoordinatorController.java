package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.entity.Student;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coordinator")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('COORDINATOR')")
public class CoordinatorController {

    private final StudentRepository studentRepository;

    @GetMapping("/students")
    public ResponseEntity<ApiResponse> getAllStudents() {
        return ResponseEntity.ok(new ApiResponse(true, "Students fetched", studentRepository.findAll()));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return ResponseEntity.ok(new ApiResponse(true, "Student fetched", student));
    }
}
