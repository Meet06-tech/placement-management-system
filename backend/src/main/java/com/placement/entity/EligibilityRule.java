package com.placement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * eligibility_rules: one record per placement drive.
 * DB design: id(BIGSERIAL PK), placement_drive_id(UNIQUE FK), min_cgpa,
 *            max_backlogs, min_graduation_year, max_graduation_year, other_requirements
 */
@Entity
@Table(name = "eligibility_rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_drive_id", unique = true, nullable = false)
    private PlacementDrive placementDrive;

    @Column(name = "min_cgpa", precision = 3, scale = 2)
    private BigDecimal minCgpa;

    @Column(name = "max_backlogs")
    private Short maxBacklogs;

    @Column(name = "min_graduation_year")
    private Short minGraduationYear;

    @Column(name = "max_graduation_year")
    private Short maxGraduationYear;

    @Column(name = "other_requirements", columnDefinition = "TEXT")
    private String otherRequirements;
}
