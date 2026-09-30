package com.placement.repository;

import com.placement.entity.PlacementDrive;
import com.placement.entity.DriveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Long> {
    List<PlacementDrive> findByStatus(DriveStatus status);
    List<PlacementDrive> findByCompanyId(Long companyId);
    List<PlacementDrive> findByCreatedById(Long coordinatorId);
}
