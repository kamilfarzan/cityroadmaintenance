package com.group3.cityroad.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Resident — can submit repair requests and track their progress.
 * Discriminator value matches RoleEnum.RESIDENT.
 */
@Entity
@DiscriminatorValue("RESIDENT")
public class Resident extends User {

    @Column
    private String residentId;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String area;

    // A resident can have many repair requests
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RepairRequest> repairRequests = new ArrayList<>();

    // --- Constructors ---

    public Resident() {}

    public Resident(String username, String name, String passwordHash,
                    String address, String phone, String area) {
        super(username, name, passwordHash);
        this.address = address;
        this.phone = phone;
        this.area = area;
    }

    // --- Methods (signatures from class diagram) ---

    public void submitRequest() {
        // delegated to RepairRequest / SchedulingService
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void trackProgress() {
        // delegated to RepairRequest / ProgressUpdate
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public String getResidentId() { return residentId; }
    public void setResidentId(String residentId) { this.residentId = residentId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public List<RepairRequest> getRepairRequests() { return repairRequests; }
    public void setRepairRequests(List<RepairRequest> repairRequests) {
        this.repairRequests = repairRequests;
    }
}