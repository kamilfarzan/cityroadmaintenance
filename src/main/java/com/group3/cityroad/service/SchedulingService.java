package com.group3.cityroad.service;

import com.group3.cityroad.dto.ScheduleResultDTO;
import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.RepairSchedule;
import com.group3.cityroad.entity.RoadAssessment;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.RepairScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SchedulingService {

    @Autowired
    private RepairScheduleRepository repairScheduleRepository;
    
    @Autowired
    private ResourceService resourceService;

    public ScheduleResultDTO scheduleRepair(RoadAssessment assessment) {
        RepairRequest request = assessment.getRepairRequest();
        RepairSchedule schedule = new RepairSchedule();
        schedule.setRepairRequest(request);
        schedule.setRequestId(String.valueOf(request.getRequestId()));
        schedule.setPriority(assessment.getPriority());
        schedule.setSupervisorId(assessment.getSupervisorId());
        
        // Simple logic: check if resources are available
        boolean resourcesAvailable = resourceService.checkAvailability(assessment.getResourceRequirement());
        
        if (resourcesAvailable) {
            schedule.setStartDate(LocalDate.now().plusDays(1));
            schedule.setEndDate(LocalDate.now().plusDays(3));
            schedule.setScheduleStatus(StatusEnum.SCHEDULED);
            schedule.setIsDeferred(false);
            
            request.setStatus(StatusEnum.SCHEDULED);
            repairScheduleRepository.save(schedule);
            return new ScheduleResultDTO(true, "Repair scheduled successfully", schedule);
        } else {
            schedule.setScheduleStatus(StatusEnum.UNDER_REVIEW); // Need alternative status from enum
            schedule.setIsDeferred(true);
            repairScheduleRepository.save(schedule);
            return new ScheduleResultDTO(false, "Insufficient resources, deferred", schedule);
        }
    }

    public void applyPriority() {
        // TBD
    }

    public void resolveConflicts() {
        // TBD
    }

    public void reschedule() {
        // Go through deferred schedules and attempt scheduling again
    }
}
