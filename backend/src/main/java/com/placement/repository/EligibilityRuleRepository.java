package com.placement.repository;

import com.placement.entity.EligibilityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EligibilityRuleRepository extends JpaRepository<EligibilityRule, Long> {
    Optional<EligibilityRule> findByPlacementDriveId(Long placementDriveId);
}
