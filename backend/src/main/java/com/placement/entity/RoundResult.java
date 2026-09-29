package com.placement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;

@Entity
@Table(
    name = "round_results",
    uniqueConstraints = @UniqueConstraint(columnNames = {"application_id", "placement_round_id"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_round_id", nullable = false)
    private PlacementRound placementRound;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RoundResultStatus result = RoundResultStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    /**
     * Coordinator who evaluated the result.
     * Nullable — may not always be set.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluated_by")
    private Coordinator evaluatedBy;

    @Column(name = "evaluated_at")
    private OffsetDateTime evaluatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
