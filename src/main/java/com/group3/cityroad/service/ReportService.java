package com.group3.cityroad.service;

import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.ResourceRequirement;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.RepairRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    public List<RepairRequest> getCompletedRepairsByDate(LocalDate start, LocalDate end) {
        if (start == null || end == null) return List.of();
        return repairRequestRepository.findByStatusAndSubmissionDateBetween(StatusEnum.COMPLETED, start, end);
    }

    public List<RepairRequest> getOutstandingRepairs() {
        return repairRequestRepository.findByStatusIn(
            List.of(StatusEnum.SUBMITTED, StatusEnum.SCHEDULED, StatusEnum.IN_PROGRESS, StatusEnum.UNDER_REVIEW)
        );
    }

    public Map<String, Integer> calculateResourceUtilization() {
        // Scans jobs mapped as active or finished to see what resources were genuinely burned
        List<RepairRequest> activeJobs = repairRequestRepository.findByStatusIn(
            List.of(StatusEnum.SCHEDULED, StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED)
        );

        Map<String, Integer> utilization = new HashMap<>();

        for (RepairRequest request : activeJobs) {
            if (request.getRoadAssessment() != null && request.getRoadAssessment().getResourceRequirement() != null) {
                ResourceRequirement reqs = request.getRoadAssessment().getResourceRequirement();
                
                if (reqs.getPersonnelQuantity() > 0 && reqs.getPersonnelType() != null) {
                    utilization.put(reqs.getPersonnelType(), 
                        utilization.getOrDefault(reqs.getPersonnelType(), 0) + reqs.getPersonnelQuantity());
                }
                
                if (reqs.getMachineQuantity() > 0 && reqs.getMachineType() != null) {
                    utilization.put(reqs.getMachineType(), 
                        utilization.getOrDefault(reqs.getMachineType(), 0) + reqs.getMachineQuantity());
                }

                if (reqs.getMaterialQuantity() > 0 && reqs.getMaterialType() != null) {
                    utilization.put(reqs.getMaterialType(), 
                        utilization.getOrDefault(reqs.getMaterialType(), 0) + reqs.getMaterialQuantity());
                }
            }
        }

        return utilization;
    }
}
