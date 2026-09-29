package com.placement.service;

import com.placement.dto.AuthResponse;
import com.placement.dto.LoginRequest;
import com.placement.dto.RegisterRequest;
import com.placement.entity.*;
import com.placement.exception.BadRequestException;
import com.placement.exception.DuplicateResourceException;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.*;
import com.placement.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateResourceException("Email already registered: " + request.email());
        }

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.branchId()));

        // 1. Create User (authentication record — no full_name here)
        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(UserRole.STUDENT)
                .isActive(true)
                .build();
        User savedUser = userRepository.save(user);

        // 2. Create Student with SAME ID via @MapsId. Hibernate derives the
        // student identifier from the managed User association on persist.
        Student student = Student.builder()
                .user(savedUser)
                .fullName(request.fullName())
                .enrollmentNumber(request.enrollmentNumber())
                .branch(branch)
                .graduationYear(request.graduationYear())
                .activeBacklogs(request.activeBacklogs())
                .build();
        studentRepository.save(student);

        String jwtToken = jwtUtil.generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

        return new AuthResponse(
                jwtToken,
                savedUser.getId(),
                savedUser.getEmail(),
                student.getFullName(),          // fullName is in students, not users
                savedUser.getRole().name(),
                savedUser.getId()               // studentId == userId for shared-PK
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (Exception e) {
            throw new BadRequestException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String fullName;
        Long entityId;

        if (user.getRole() == UserRole.STUDENT) {
            Student student = studentRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
            fullName = student.getFullName();
            entityId = student.getId();    // same as user.getId()
        } else {
            Coordinator coordinator = coordinatorRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coordinator profile not found"));
            fullName = coordinator.getFullName();
            entityId = coordinator.getId();
        }

        String jwtToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return new AuthResponse(
                jwtToken,
                user.getId(),
                user.getEmail(),
                fullName,
                user.getRole().name(),
                entityId
        );
    }
}
