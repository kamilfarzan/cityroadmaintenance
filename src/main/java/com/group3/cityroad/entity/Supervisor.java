package com.group3.cityroad.entity;

import jakarta.persistence.*;
/**
 * Supervisor — manages repair requests for their assigned branch office.
 * Can assess requests, update progress, and view schedules.
 */
@Entity
@DiscriminatorValue("SUPERVISOR")
public class Supervisor extends User {

    @Column
    private String supervisorId;

    @Column
    private String branchOfficeId;   // FK reference — kept as String for loose coupling in skeleton

    // --- Constructors ---

    public Supervisor() {}

    public Supervisor(String username, String name, String passwordHash, String branchOfficeId) {
        super(username, name, passwordHash);
        this.branchOfficeId = branchOfficeId;
    }

    // --- Methods (signatures from class diagram) ---


    public void assignPriority() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateProgress() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void enterAssessment() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }

    public String getBranchOfficeId() { return branchOfficeId; }
    public void setBranchOfficeId(String branchOfficeId) { this.branchOfficeId = branchOfficeId; }
}