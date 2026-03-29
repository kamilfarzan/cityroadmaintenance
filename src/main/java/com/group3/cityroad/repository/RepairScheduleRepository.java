package com.group3.cityroad.repository;

import com.group3.cityroad.entity.RepairSchedule;
import com.group3.cityroad.enums.PriorityEnum;
import com.group3.cityroad.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairScheduleRepository extends JpaRepository<RepairSchedule, Long> {
    List<RepairSchedule> findByScheduleStatus(StatusEnum status);
    List<RepairSchedule> findByPriority(PriorityEnum priority);
    List<RepairSchedule> findBySupervisorId(String supervisorId);
}
