package com.group3.cityroad.service;

import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.RoadAssessment;
import com.group3.cityroad.entity.ResourceRequirement;
import com.group3.cityroad.enums.LocalityTypeEnum;
import com.group3.cityroad.enums.PriorityEnum;
import com.group3.cityroad.enums.SeverityEnum;
import com.group3.cityroad.repository.RepairRequestRepository;
import com.group3.cityroad.repository.RoadAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AssessmentService {

    @Autowired
    private RoadAssessmentRepository roadAssessmentRepository;
    
    @Autowired
    private RepairRequestRepository repairRequestRepository;
    
    @Autowired
    private SchedulingService schedulingService;

    public RoadAssessment createAssessment(RepairRequest request, String supervisorId, SeverityEnum severity, LocalityTypeEnum locality) {
        RoadAssessment assessment = new RoadAssessment();
        assessment.setRepairRequest(request);
        assessment.setRequestId(String.valueOf(request.getRequestId()));
        assessment.setSupervisorId(supervisorId);
        assessment.setAssessmentDate(LocalDate.now());
        assessment.setSeverity(severity);
        assessment.setLocalityType(locality);
        
        PriorityEnum priority = assignPriority(severity, locality);
        assessment.setPriority(priority);
        
        return assessment;
    }

    private PriorityEnum assignPriority(SeverityEnum severity, LocalityTypeEnum locality) {
        if (severity == SeverityEnum.EMERGENCY) return PriorityEnum.CRITICAL;
        if (severity == SeverityEnum.CRITICAL) return PriorityEnum.HIGH;
        if (severity == SeverityEnum.MAJOR) return PriorityEnum.MEDIUM;
        if (severity == SeverityEnum.MODERATE) return PriorityEnum.LOW;
        return PriorityEnum.DEFERRED;
    }

    public void submitAssessment(RoadAssessment assessment, ResourceRequirement reqs, LocalDate startDate, LocalDate endDate) {
        assessment.setResourceRequirement(reqs);
        reqs.setRoadAssessment(assessment);
        
        roadAssessmentRepository.save(assessment);
        
        // Trigger scheduling with custom dates
        schedulingService.scheduleRepair(assessment, startDate, endDate);
    }
}
