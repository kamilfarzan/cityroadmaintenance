package com.group3.cityroad.entity;

import com.group3.cityroad.enums.StatusEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ProgressUpdate — posted by a Supervisor to track repair work progress.
 * Visible to residents via the public portal.
 * Belongs to one RepairRequest. Many updates per request.
 */
@Entity
@Table(name = "progress_update")
public class ProgressUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long updateId;

    @Column(nullable = false)
    private String requestId;        // denormalized for quick lookup

    @Column(nullable = false)
    private String supervisorId;

    @Column(nullable = false)
    private Float progressPct;       // 0.0 to 100.0

    @Column(length = 1000)
    private String statusNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum statusFlag;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private Boolean isComplete;

    // --- Relationships ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_request_id", nullable = false)
    private RepairRequest repairRequest;

    // --- Constructors ---

    public ProgressUpdate() {}

    public ProgressUpdate(RepairRequest repairRequest, String supervisorId,
                          Float progressPct, String statusNote) {
        this.repairRequest = repairRequest;
        this.requestId = String.valueOf(repairRequest.getRequestId());
        this.supervisorId = supervisorId;
        this.progressPct = progressPct;
        this.statusNote = statusNote;
        this.timestamp = LocalDateTime.now();
        this.isComplete = false;
        this.statusFlag = StatusEnum.IN_PROGRESS;
    }

    // --- Methods (signatures from class diagram) ---

    public void addUpdate() {
        this.timestamp = LocalDateTime.now();
    }

    public void markComplete() {
        this.isComplete = true;
        this.statusFlag = StatusEnum.COMPLETED;
        this.progressPct = 100.0f;
    }

    public void recordBlockedStatus(String reason) {
        this.statusFlag = StatusEnum.CLOSED;   // use CLOSED as BLOCKED proxy for now
        this.statusNote = reason;
    }

    public java.util.List<ProgressUpdate> getHistory() {
        throw new UnsupportedOperationException("Use ProgressUpdateRepository.findByRepairRequest()");
    }

    // --- Getters and Setters ---

    public Long getUpdateId() { return updateId; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }

    public Float getProgressPct() { return progressPct; }
    public void setProgressPct(Float progressPct) { this.progressPct = progressPct; }

    public String getStatusNote() { return statusNote; }
    public void setStatusNote(String statusNote) { this.statusNote = statusNote; }

    public StatusEnum getStatusFlag() { return statusFlag; }
    public void setStatusFlag(StatusEnum statusFlag) { this.statusFlag = statusFlag; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Boolean getIsComplete() { return isComplete; }
    public void setIsComplete(Boolean isComplete) { this.isComplete = isComplete; }

    public RepairRequest getRepairRequest() { return repairRequest; }
    public void setRepairRequest(RepairRequest repairRequest) {
        this.repairRequest = repairRequest;
    }
}