package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.StudentProfileResponse;
import com.placement.dto.UpdateStudentRequest;
import com.placement.entity.Branch;
import com.placement.entity.Skill;
import com.placement.entity.User;
import com.placement.repository.UserRepository;
import com.placement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).map(User::getId).orElse(null);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile() {
        StudentProfileResponse profile = studentService.getProfileByUserId(getCurrentUserId());
        return ResponseEntity.ok(new ApiResponse(true, "Profile fetched", profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody UpdateStudentRequest request) {
        StudentProfileResponse profile = studentService.updateProfile(getCurrentUserId(), request);
        return ResponseEntity.ok(new ApiResponse(true, "Profile updated", profile));
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse> getMySkills() {
        StudentProfileResponse profile = studentService.getProfileByUserId(getCurrentUserId());
        List<Skill> skills = studentService.getSkills(profile.id());
        return ResponseEntity.ok(new ApiResponse(true, "Skills fetched", skills));
    }

    @PutMapping("/skills")
    public ResponseEntity<ApiResponse> updateMySkills(@RequestBody List<Long> skillIds) {
        StudentProfileResponse profile = studentService.updateSkills(getCurrentUserId(), skillIds);
        return ResponseEntity.ok(new ApiResponse(true, "Skills updated", profile));
    }
}
