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
    private Integer personnelQuantity = 0;
    
    @Column
    private String personnelType;

    @Column(nullable = false)
    private Integer machineQuantity = 0;
    
    @Column
    private String machineType;

    @Column(nullable = false)
    private Integer materialQuantity = 0;
    
    @Column
    private String materialType;

    // --- Relationships ---

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id_fk", nullable = false)
    private RoadAssessment roadAssessment;

    // --- Constructors ---

    public ResourceRequirement() {}

    public ResourceRequirement(RoadAssessment roadAssessment, 
                               Integer personnelQuantity, String personnelType,
                               Integer machineQuantity, String machineType,
                               Integer materialQuantity, String materialType) {
        this.roadAssessment = roadAssessment;
        this.assessmentId = String.valueOf(roadAssessment.getAssessmentId());
        
        this.personnelQuantity = personnelQuantity;
        this.personnelType = personnelType;
        
        this.machineQuantity = machineQuantity;
        this.machineType = machineType;
        
        this.materialQuantity = materialQuantity;
        this.materialType = materialType;
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

    public Integer getPersonnelQuantity() { return personnelQuantity; }
    public void setPersonnelQuantity(Integer personnelQuantity) { this.personnelQuantity = personnelQuantity; }

    public String getPersonnelType() { return personnelType; }
    public void setPersonnelType(String personnelType) { this.personnelType = personnelType; }

    public Integer getMachineQuantity() { return machineQuantity; }
    public void setMachineQuantity(Integer machineQuantity) { this.machineQuantity = machineQuantity; }

    public String getMachineType() { return machineType; }
    public void setMachineType(String machineType) { this.machineType = machineType; }

    public Integer getMaterialQuantity() { return materialQuantity; }
    public void setMaterialQuantity(Integer materialQuantity) { this.materialQuantity = materialQuantity; }

    public String getMaterialType() { return materialType; }
    public void setMaterialType(String materialType) { this.materialType = materialType; }

    public RoadAssessment getRoadAssessment() { return roadAssessment; }
    public void setRoadAssessment(RoadAssessment roadAssessment) {
        this.roadAssessment = roadAssessment;
    }
}