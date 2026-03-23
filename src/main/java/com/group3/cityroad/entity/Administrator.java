package com.group3.cityroad.entity;

import jakarta.persistence.*;

/**
 * Administrator — manages system resources (manpower, machines, materials).
 */
@Entity
@DiscriminatorValue("ADMINISTRATOR")
public class Administrator extends User {

    @Column
    private String adminId;

    // --- Constructors ---

    public Administrator() {}

    public Administrator(String username, String name, String passwordHash) {
        super(username, name, passwordHash);
    }

    // --- Methods (signatures from class diagram) ---

    public void manageResources() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateManpower() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateMachines() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateReschedule() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
}