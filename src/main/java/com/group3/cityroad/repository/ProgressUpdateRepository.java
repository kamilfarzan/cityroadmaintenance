package com.group3.cityroad.repository;

import com.group3.cityroad.entity.ProgressUpdate;
import com.group3.cityroad.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressUpdateRepository extends JpaRepository<ProgressUpdate, Long> {
    List<ProgressUpdate> findByRepairRequestOrderByTimestampDesc(RepairRequest repairRequest);
}
