package com.placement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "placement_drives")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacementDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "job_role", nullable = false, length = 150)
    private String jobRole;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "min_package_lpa", precision = 6, scale = 2)
    private BigDecimal minPackageLpa;

    @Column(name = "max_package_lpa", precision = 6, scale = 2)
    private BigDecimal maxPackageLpa;

    @Column(length = 150)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "drive_type", nullable = false)
    private DriveType driveType;

    @Column(name = "application_deadline", nullable = false)
    private OffsetDateTime applicationDeadline;

    @Column(name = "drive_date")
    private LocalDate driveDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DriveStatus status = DriveStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Coordinator createdBy;

    /** Eligibility rules — one-to-one, mapped on the EligibilityRule side */
    @OneToOne(mappedBy = "placementDrive", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EligibilityRule eligibilityRule;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "drive_eligible_branches",
        joinColumns = @JoinColumn(name = "placement_drive_id"),
        inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    @Builder.Default
    private Set<Branch> eligibleBranches = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "drive_required_skills",
        joinColumns = @JoinColumn(name = "placement_drive_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @Builder.Default
    private Set<Skill> requiredSkills = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
