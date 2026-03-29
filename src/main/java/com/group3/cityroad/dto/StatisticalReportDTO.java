package com.group3.cityroad.dto;

import java.util.Map;

public class StatisticalReportDTO {

    private long repairsCompleted;
    private long outstandingRepairs;
    private Map<String, Double> resourceUtilization;

    public StatisticalReportDTO() {}

    public StatisticalReportDTO(long repairsCompleted, long outstandingRepairs, Map<String, Double> resourceUtilization) {
        this.repairsCompleted = repairsCompleted;
        this.outstandingRepairs = outstandingRepairs;
        this.resourceUtilization = resourceUtilization;
    }

    public long getRepairsCompleted() {
        return repairsCompleted;
    }

    public void setRepairsCompleted(long repairsCompleted) {
        this.repairsCompleted = repairsCompleted;
    }

    public long getOutstandingRepairs() {
        return outstandingRepairs;
    }

    public void setOutstandingRepairs(long outstandingRepairs) {
        this.outstandingRepairs = outstandingRepairs;
    }

    public Map<String, Double> getResourceUtilization() {
        return resourceUtilization;
    }

    public void setResourceUtilization(Map<String, Double> resourceUtilization) {
        this.resourceUtilization = resourceUtilization;
    }
}
