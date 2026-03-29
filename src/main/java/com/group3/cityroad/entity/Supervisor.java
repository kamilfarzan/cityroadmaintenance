package com.group3.cityroad.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Supervisor — manages repair requests for their assigned branch office.
 * Can assess requests, update progress, and view schedules.
 *
 * Relationship: Many supervisors can belong to one BranchOffice.
 */
@Entity
@DiscriminatorValue("SUPERVISOR")
public class Supervisor extends User {

    @Column
    private String supervisorId;

    // Proper JPA association — replaces the old branchOfficeId: String
    // LAZY fetch: BranchOffice data is only loaded from DB when you actually call getBranchOffice()
    // nullable = false removed because single-table inheritance requires subclass fields to be nullable in DB
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_office_id")
    private BranchOffice branchOffice;

    // --- Constructors ---

    public Supervisor() {}

    public Supervisor(String username, String name, String passwordHash,
                      BranchOffice branchOffice) {
        super(username, name, passwordHash);
        this.branchOffice = branchOffice;
    }

    // --- Methods (signatures from class diagram) ---

    public List<RepairRequest> viewNewRequests() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void assignPriority() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateProgress() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<RepairSchedule> viewSchedules() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void enterAssessment() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }

    public BranchOffice getBranchOffice() { return branchOffice; }
    public void setBranchOffice(BranchOffice branchOffice) { this.branchOffice = branchOffice; }
}