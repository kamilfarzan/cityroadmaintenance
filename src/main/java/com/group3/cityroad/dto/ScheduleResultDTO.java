package com.group3.cityroad.dto;

import com.group3.cityroad.entity.RepairSchedule;

public class ScheduleResultDTO {
    
    private boolean successful;
    private String message;
    private RepairSchedule schedule;

    public ScheduleResultDTO() {}

    public ScheduleResultDTO(boolean successful, String message, RepairSchedule schedule) {
        this.successful = successful;
        this.message = message;
        this.schedule = schedule;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RepairSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(RepairSchedule schedule) {
        this.schedule = schedule;
    }
}
