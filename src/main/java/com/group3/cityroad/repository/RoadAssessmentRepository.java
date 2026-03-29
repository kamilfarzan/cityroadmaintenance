package com.group3.cityroad.repository;

import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.RoadAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoadAssessmentRepository extends JpaRepository<RoadAssessment, Long> {
    Optional<RoadAssessment> findByRepairRequest(RepairRequest repairRequest);
}
