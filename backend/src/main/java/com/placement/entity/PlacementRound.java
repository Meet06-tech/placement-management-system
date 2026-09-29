package com.placement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(
    name = "placement_rounds",
    uniqueConstraints = @UniqueConstraint(columnNames = {"placement_drive_id", "round_number"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacementRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_drive_id", nullable = false)
    private PlacementDrive placementDrive;

    @Column(name = "round_number", nullable = false)
    private Short roundNumber;

    @Column(name = "round_name", nullable = false, length = 100)
    private String roundName;

    @Enumerated(EnumType.STRING)
    @Column(name = "round_type", nullable = false)
    private RoundType roundType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "round_date")
    private LocalDate roundDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RoundStatus status = RoundStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
