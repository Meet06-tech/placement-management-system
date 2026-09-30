package com.placement.repository;

import com.placement.entity.RoundResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoundResultRepository extends JpaRepository<RoundResult, Long> {
    List<RoundResult> findByApplicationId(Long applicationId);
    Optional<RoundResult> findByApplicationIdAndPlacementRoundId(Long applicationId, Long placementRoundId);
    List<RoundResult> findByPlacementRoundId(Long placementRoundId);
}
