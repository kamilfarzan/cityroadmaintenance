package com.group3.cityroad.entity;

import jakarta.persistence.*;

/**
 * Machine — a specific type of Resource representing equipment.
 * Examples: excavators, road rollers, concrete mixers, dump trucks.
 */
@Entity
@DiscriminatorValue("MACHINE")
public class Machine extends Resource {

    @Column
    private String machineType;      // e.g. "Excavator", "Road Roller", "Concrete Mixer"

    @Column
    private String machineId;        // physical asset ID / serial number

    @Column
    private String operationalStatus; // e.g. "OPERATIONAL", "UNDER_MAINTENANCE", "BROKEN_DOWN"

    @Column
    private Float utilization;       // 0.0 to 1.0

    // --- Constructors ---

    public Machine() {}

    public Machine(String name, String machineType, String machineId, Integer quantity) {
        super(name, quantity);
        this.machineType = machineType;
        this.machineId = machineId;
        this.operationalStatus = "OPERATIONAL";
        this.utilization = 0.0f;
    }

    // --- Methods (signatures from class diagram) ---

    public void assign() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void release() {
        this.operationalStatus = "OPERATIONAL";
        this.utilization = 0.0f;
    }

    @Override
    public float getUtilization() {
        return this.utilization != null ? this.utilization : 0.0f;
    }

    // --- Getters and Setters ---

    public String getMachineType() { return machineType; }
    public void setMachineType(String machineType) { this.machineType = machineType; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public String getOperationalStatus() { return operationalStatus; }
    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public void setUtilization(Float utilization) { this.utilization = utilization; }
}