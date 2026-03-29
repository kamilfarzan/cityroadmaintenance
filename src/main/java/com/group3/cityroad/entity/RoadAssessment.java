package com.group3.cityroad.entity;

import com.group3.cityroad.enums.LocalityTypeEnum;
import com.group3.cityroad.enums.PriorityEnum;
import com.group3.cityroad.enums.SeverityEnum;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * RoadAssessment — created by a Supervisor after visiting the road site.
 * Belongs to exactly one RepairRequest.
 * Has one ResourceRequirement.
 */
@Entity
@Table(name = "road_assessment")
public class RoadAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @Column(nullable = false)
    private String requestId;        // denormalized for quick lookup

    @Column(nullable = false)
    private String supervisorId;

    @Enumerated(EnumType.STRING)
    @Column
    private SeverityEnum severity;

    @Enumerated(EnumType.STRING)
    @Column
    private PriorityEnum priority;

    @Enumerated(EnumType.STRING)
    @Column
    private LocalityTypeEnum localityType;

    @Column
    private Boolean available;

    @Column
    private LocalDate assessmentDate;

    // --- Relationships ---

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_request_id", nullable = false)
    private RepairRequest repairRequest;

    @OneToOne(mappedBy = "roadAssessment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ResourceRequirement resourceRequirement;

    // --- Constructors ---

    public RoadAssessment() {}

    public RoadAssessment(RepairRequest repairRequest, String supervisorId) {
        this.repairRequest = repairRequest;
        this.requestId = String.valueOf(repairRequest.getRequestId());
        this.supervisorId = supervisorId;
        this.assessmentDate = LocalDate.now();
    }

    // --- Methods (signatures from class diagram) ---

    public void recordSeverity(SeverityEnum severity) {
        this.severity = severity;
    }

    public void assignPriority(PriorityEnum priority, LocalityTypeEnum localityType) {
        this.priority = priority;
        this.localityType = localityType;
    }

    public void isAvailable(boolean available) {
        this.available = available;
    }

    public void submit() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public Long getAssessmentId() { return assessmentId; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }

    public SeverityEnum getSeverity() { return severity; }
    public void setSeverity(SeverityEnum severity) { this.severity = severity; }

    public PriorityEnum getPriority() { return priority; }
    public void setPriority(PriorityEnum priority) { this.priority = priority; }

    public LocalityTypeEnum getLocalityType() { return localityType; }
    public void setLocalityType(LocalityTypeEnum localityType) { this.localityType = localityType; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public LocalDate getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public RepairRequest getRepairRequest() { return repairRequest; }
    public void setRepairRequest(RepairRequest repairRequest) {
        this.repairRequest = repairRequest;
    }

    public ResourceRequirement getResourceRequirement() { return resourceRequirement; }
    public void setResourceRequirement(ResourceRequirement resourceRequirement) {
        this.resourceRequirement = resourceRequirement;
    }
}