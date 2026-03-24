package com.group3.cityroad.entity;

import jakarta.persistence.*;

/**
 * ResourceRequirement — specifies what is needed to complete a repair.
 * Created by a Supervisor during assessment (UC4).
 * Belongs to exactly one RoadAssessment.
 */
@Entity
@Table(name = "resource_requirement")
public class ResourceRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requirementId;

    @Column
    private String assessmentId;     // denormalized for quick lookup

    @Column(nullable = false)
    private Integer quantityNeeded;

    @Column
    private String machineType;

    @Column
    private String materialType;

    @Column
    private String personnelType;

    // --- Relationships ---

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id_fk", nullable = false)
    private RoadAssessment roadAssessment;

    // --- Constructors ---

    public ResourceRequirement() {}

    public ResourceRequirement(RoadAssessment roadAssessment, Integer quantityNeeded,
                                String machineType, String materialType, String personnelType) {
        this.roadAssessment = roadAssessment;
        this.assessmentId = String.valueOf(roadAssessment.getAssessmentId());
        this.quantityNeeded = quantityNeeded;
        this.machineType = machineType;
        this.materialType = materialType;
        this.personnelType = personnelType;
    }

    // --- Methods (signatures from class diagram) ---

    public boolean checkAvailability() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void allocate() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public Long getRequirementId() { return requirementId; }

    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }

    public Integer getQuantityNeeded() { return quantityNeeded; }
    public void setQuantityNeeded(Integer quantityNeeded) { this.quantityNeeded = quantityNeeded; }

    public String getMachineType() { return machineType; }
    public void setMachineType(String machineType) { this.machineType = machineType; }

    public String getMaterialType() { return materialType; }
    public void setMaterialType(String materialType) { this.materialType = materialType; }

    public String getPersonnelType() { return personnelType; }
    public void setPersonnelType(String personnelType) { this.personnelType = personnelType; }

    public RoadAssessment getRoadAssessment() { return roadAssessment; }
    public void setRoadAssessment(RoadAssessment roadAssessment) {
        this.roadAssessment = roadAssessment;
    }
}