package com.placement.service;

import com.placement.dto.StudentProfileResponse;
import com.placement.dto.UpdateStudentRequest;
import com.placement.entity.*;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final BranchRepository branchRepository;

    // Fetch by userId (== studentId in shared-PK design)
    public StudentProfileResponse getProfileByUserId(Long userId) {
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return mapToResponse(student);
    }

    @Transactional
    public StudentProfileResponse updateProfile(Long userId, UpdateStudentRequest request) {
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (request.fullName() != null) student.setFullName(request.fullName());
        if (request.phone() != null) student.setPhone(request.phone());
        if (request.cgpa() != null) student.setCgpa(request.cgpa());
        if (request.activeBacklogs() != null) student.setActiveBacklogs(request.activeBacklogs());
        if (request.currentSemester() != null) student.setCurrentSemester(request.currentSemester());
        if (request.dateOfBirth() != null) student.setDateOfBirth(request.dateOfBirth());
        if (request.gender() != null) student.setGender(request.gender());
        if (request.address() != null) student.setAddress(request.address());
        if (request.branchId() != null) {
            Branch branch = branchRepository.findById(request.branchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
            student.setBranch(branch);
        }
        return mapToResponse(studentRepository.save(student));
    }

    public List<Skill> getSkills(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return List.copyOf(student.getSkills());
    }

    @Transactional
    public StudentProfileResponse updateSkills(Long userId, List<Long> skillIds) {
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Set<Skill> skills = skillIds.stream()
                .map(id -> skillRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + id)))
                .collect(Collectors.toSet());

        student.setSkills(skills);
        return mapToResponse(studentRepository.save(student));
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    private StudentProfileResponse mapToResponse(Student student) {
        Branch branch = student.getBranch();
        List<StudentProfileResponse.SkillDto> skillDtos = student.getSkills().stream()
                .map(s -> new StudentProfileResponse.SkillDto(s.getId(), s.getName()))
                .collect(Collectors.toList());

        return new StudentProfileResponse(
                student.getId(),
                student.getFullName(),
                student.getUser().getEmail(),
                student.getEnrollmentNumber(),
                branch != null ? branch.getName() : null,
                branch != null ? branch.getCode() : null,
                branch != null ? branch.getId() : null,
                student.getCgpa(),
                student.getPhone(),
                student.getActiveBacklogs(),
                student.getCurrentSemester(),
                student.getGraduationYear(),
                student.getGender(),
                student.getAddress(),
                student.getResumeFileUrl(),
                student.getProfilePhotoUrl(),
                skillDtos
        );
    }
}
