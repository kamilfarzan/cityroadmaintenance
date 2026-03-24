package com.group3.cityroad.entity;

import jakarta.persistence.*;

/**
 * Manpower — a specific type of Resource representing personnel.
 * Examples: road workers, engineers, safety officers.
 */
@Entity
@DiscriminatorValue("MANPOWER")
public class Manpower extends Resource {

    @Column
    private String personnelType;    // e.g. "Road Worker", "Engineer", "Safety Officer"

    @Column
    private String assignedJobId;    // which RepairSchedule this person is currently on

    @Column
    private Float utilization;       // 0.0 to 1.0 — percentage of time currently in use

    // --- Constructors ---

    public Manpower() {}

    public Manpower(String name, String personnelType, Integer quantity) {
        super(name, quantity);
        this.personnelType = personnelType;
        this.utilization = 0.0f;
    }

    // --- Methods (signatures from class diagram) ---

    public void assign() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void release() {
        this.assignedJobId = null;
        this.utilization = 0.0f;
    }

    @Override
    public float getUtilization() {
        return this.utilization != null ? this.utilization : 0.0f;
    }

    // --- Getters and Setters ---

    public String getPersonnelType() { return personnelType; }
    public void setPersonnelType(String personnelType) { this.personnelType = personnelType; }

    public String getAssignedJobId() { return assignedJobId; }
    public void setAssignedJobId(String assignedJobId) { this.assignedJobId = assignedJobId; }

    public void setUtilization(Float utilization) { this.utilization = utilization; }
}