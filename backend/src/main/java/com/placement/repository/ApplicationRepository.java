package com.placement.repository;

import com.placement.entity.Application;
import com.placement.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);
    List<Application> findByPlacementDriveId(Long placementDriveId);
    Optional<Application> findByStudentIdAndPlacementDriveId(Long studentId, Long placementDriveId);
    long countByStatus(ApplicationStatus status);
}
