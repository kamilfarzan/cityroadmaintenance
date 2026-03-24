package com.group3.cityroad.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BranchOffice — a geographical subdivision of the city.
 * Repair requests are routed to the branch office matching their location.
 * Supervisors are assigned to a branch office.
 */
@Entity
@Table(name = "branch_office")
public class BranchOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String suburb;

    @Column(nullable = false)
    private String areaJurisdiction;   // geographic area this office covers

    @Column
    private String branchManagerName;

    // Repair requests assigned to this branch office
    @OneToMany(mappedBy = "branchOffice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RepairRequest> repairRequests = new ArrayList<>();

    // --- Constructors ---

    public BranchOffice() {}

    public BranchOffice(String name, String suburb, String areaJurisdiction) {
        this.name = name;
        this.suburb = suburb;
        this.areaJurisdiction = areaJurisdiction;
    }

    // --- Methods (signatures from class diagram) ---

    public List<RepairRequest> getNewRequests() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void assignSupervisor() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getArea() {
        return this.areaJurisdiction;
    }

    // --- Getters and Setters ---

    public Long getBranchId() { return branchId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSuburb() { return suburb; }
    public void setSuburb(String suburb) { this.suburb = suburb; }

    public String getAreaJurisdiction() { return areaJurisdiction; }
    public void setAreaJurisdiction(String areaJurisdiction) {
        this.areaJurisdiction = areaJurisdiction;
    }

    public String getBranchManagerName() { return branchManagerName; }
    public void setBranchManagerName(String branchManagerName) {
        this.branchManagerName = branchManagerName;
    }

    public List<RepairRequest> getRepairRequests() { return repairRequests; }
    public void setRepairRequests(List<RepairRequest> repairRequests) {
        this.repairRequests = repairRequests;
    }
}