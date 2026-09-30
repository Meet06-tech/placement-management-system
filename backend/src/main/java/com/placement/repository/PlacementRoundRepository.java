package com.placement.repository;

import com.placement.entity.PlacementRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlacementRoundRepository extends JpaRepository<PlacementRound, Long> {
    List<PlacementRound> findByPlacementDriveIdOrderByRoundNumber(Long placementDriveId);
}
