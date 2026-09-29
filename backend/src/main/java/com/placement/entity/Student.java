package com.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    /**
     * students.id is simultaneously the PK and FK to users.id.
     * No @GeneratedValue — the ID is provided by the User it shares.
     */
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "enrollment_number", unique = true, nullable = false, length = 20)
    private String enrollmentNumber;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone", length = 15)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "graduation_year", nullable = false)
    private Short graduationYear;

    @Column(name = "current_semester")
    @Min(1)
    @Max(8)
    private Short currentSemester;

    @Column(name = "cgpa", precision = 3, scale = 2)
    @DecimalMin("0.00")
    @DecimalMax("10.00")
    private BigDecimal cgpa;

    @Column(name = "active_backlogs", nullable = false)
    @Min(0)
    @Builder.Default
    private Short activeBacklogs = 0;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "resume_file_url", columnDefinition = "TEXT")
    private String resumeFileUrl;

    @Column(name = "profile_photo_url", columnDefinition = "TEXT")
    private String profilePhotoUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_skills",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @Builder.Default
    private Set<Skill> skills = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
