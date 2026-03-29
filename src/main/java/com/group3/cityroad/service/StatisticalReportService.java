package com.group3.cityroad.service;

import com.group3.cityroad.dto.StatisticalReportDTO;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.RepairRequestRepository;
import com.group3.cityroad.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;

@Service
public class StatisticalReportService {

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    public StatisticalReportDTO getRepairStats(LocalDate startDate, LocalDate endDate) {
        long completed = repairRequestRepository.findAll().stream()
                .filter(r -> r.getStatus() == StatusEnum.COMPLETED &&
                        r.getSubmissionDate().isAfter(startDate) &&
                        r.getSubmissionDate().isBefore(endDate))
                .count();

        long outstanding = getOutstanding();

        return new StatisticalReportDTO(completed, outstanding, getUtilizationStats());
    }

    public long getOutstanding() {
        return repairRequestRepository.findAll().stream()
                .filter(r -> r.getStatus() == StatusEnum.SUBMITTED || 
                             r.getStatus() == StatusEnum.UNDER_REVIEW ||
                             r.getStatus() == StatusEnum.SCHEDULED ||
                             r.getStatus() == StatusEnum.IN_PROGRESS)
                .count();
    }

    public HashMap<String, Double> getUtilizationStats() {
        // Simplified map
        HashMap<String, Double> util = new HashMap<>();
        util.put("MANPOWER", 85.0);
        util.put("MACHINE", 70.0);
        util.put("RAW_MATERIAL", 40.0);
        return util;
    }
}
