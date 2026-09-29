package com.placement.repository;

import com.placement.entity.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {
    // With shared-PK (@MapsId), coordinator.id == user.id — use findById(userId) directly.
    Optional<Coordinator> findByUserEmail(String email);
}
