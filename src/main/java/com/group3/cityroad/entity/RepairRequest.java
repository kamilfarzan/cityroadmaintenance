package com.group3.cityroad.entity;

import com.group3.cityroad.enums.StatusEnum;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * RepairRequest — submitted by a Resident for a damaged road.
 * Routed to a BranchOffice based on location.
 * Has one RoadAssessment, one RepairSchedule, and many ProgressUpdates.
 */
@Entity
@Table(name = "repair_request")
public class RepairRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(nullable = false)
    private String roadLocation;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String areaJurisdiction;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private String residentId;     // denormalized for quick lookup

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;

    // --- Relationships ---

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident_id_fk")
    private Resident resident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_office_id")
    private BranchOffice branchOffice;

    @OneToOne(mappedBy = "repairRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RoadAssessment roadAssessment;

    @OneToOne(mappedBy = "repairRequest", cascade = CascadeType.ALL)
    private RepairSchedule repairSchedule;

    @OneToMany(mappedBy = "repairRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProgressUpdate> progressUpdates = new ArrayList<>();

    // --- Constructors ---

    public RepairRequest() {}

    public RepairRequest(String roadLocation, String description,
                         String areaJurisdiction, Resident resident) {
        this.roadLocation = roadLocation;
        this.description = description;
        this.areaJurisdiction = areaJurisdiction;
        this.resident = resident;
        this.submissionDate = LocalDate.now();
        this.status = StatusEnum.SUBMITTED;
    }

    // --- Methods (signatures from class diagram) ---

    public void submit() {
        this.status = StatusEnum.SUBMITTED;
    }

    public boolean validate() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public StatusEnum getStatus() {
        return this.status;
    }

    public void updateStatus(StatusEnum newStatus) {
        this.status = newStatus;
    }

    // --- Getters and Setters ---

    public Long getRequestId() { return requestId; }

    public String getRoadLocation() { return roadLocation; }
    public void setRoadLocation(String roadLocation) { this.roadLocation = roadLocation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAreaJurisdiction() { return areaJurisdiction; }
    public void setAreaJurisdiction(String areaJurisdiction) {
        this.areaJurisdiction = areaJurisdiction;
    }

    public LocalDate getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getResidentId() { return residentId; }
    public void setResidentId(String residentId) { this.residentId = residentId; }

    public void setStatus(StatusEnum status) { this.status = status; }

    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }

    public BranchOffice getBranchOffice() { return branchOffice; }
    public void setBranchOffice(BranchOffice branchOffice) { this.branchOffice = branchOffice; }

    public RoadAssessment getRoadAssessment() { return roadAssessment; }
    public void setRoadAssessment(RoadAssessment roadAssessment) {
        this.roadAssessment = roadAssessment;
    }

    public RepairSchedule getRepairSchedule() { return repairSchedule; }
    public void setRepairSchedule(RepairSchedule repairSchedule) {
        this.repairSchedule = repairSchedule;
    }

    public List<ProgressUpdate> getProgressUpdates() { return progressUpdates; }
    public void setProgressUpdates(List<ProgressUpdate> progressUpdates) {
        this.progressUpdates = progressUpdates;
    }
}