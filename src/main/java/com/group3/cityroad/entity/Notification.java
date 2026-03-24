package com.group3.cityroad.entity;

import com.group3.cityroad.enums.NotifTypeEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Notification — created by NotificationService when a key event occurs.
 * Key events: REQUEST_SUBMITTED (UC2 extend) and REPAIR_COMPLETED (UC6 extend).
 * Belongs to one Resident (the recipient).
 */
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String residentId;       // recipient

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotifTypeEnum type;

    @Column(nullable = false)
    private String notifStatus;      // "SENT", "FAILED", "SKIPPED", "PENDING_RETRY"

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime sentAt;

    @Column
    private Integer retryCount;

    // --- Relationships ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id_fk", nullable = false)
    private Resident resident;

    // --- Constructors ---

    public Notification() {}

    public Notification(Resident resident, NotifTypeEnum type, String message) {
        this.resident = resident;
        this.residentId = String.valueOf(resident.getUserId());
        this.type = type;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.notifStatus = "PENDING_RETRY";
        this.retryCount = 0;
    }

    // --- Methods (signatures from class diagram) ---

    public void send() {
        throw new UnsupportedOperationException("Use NotificationService.sendNotification()");
    }

    public void retry() {
        if (this.retryCount == null) this.retryCount = 0;
        this.retryCount++;
    }

    public void logStatus(String status) {
        this.notifStatus = status;
        if ("SENT".equals(status)) {
            this.sentAt = LocalDateTime.now();
        }
    }

    // --- Getters and Setters ---

    public Long getNotificationId() { return notificationId; }

    public String getResidentId() { return residentId; }
    public void setResidentId(String residentId) { this.residentId = residentId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotifTypeEnum getType() { return type; }
    public void setType(NotifTypeEnum type) { this.type = type; }

    public String getNotifStatus() { return notifStatus; }
    public void setNotifStatus(String notifStatus) { this.notifStatus = notifStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
}