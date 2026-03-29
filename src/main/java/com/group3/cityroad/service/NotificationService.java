package com.group3.cityroad.service;

import com.group3.cityroad.entity.Notification;
import com.group3.cityroad.entity.Resident;
import com.group3.cityroad.enums.NotifTypeEnum;
import com.group3.cityroad.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(Resident resident, String message, NotifTypeEnum type) {
        Notification notification = new Notification();
        notification.setResident(resident);
        notification.setResidentId(String.valueOf(resident.getUserId()));
        notification.setMessage(message);
        notification.setType(type);
        notification.setNotifStatus("SENT");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSentAt(LocalDateTime.now());
        notification.setRetryCount(0);
        
        System.out.println("Notification sent to " + resident.getName() + ": " + message);
        notificationRepository.save(notification);
    }

    public void scheduleRetry(Notification notification) {
        notification.setRetryCount(notification.getRetryCount() + 1);
        notification.setNotifStatus("PENDING_RETRY");
        notificationRepository.save(notification);
    }

    public void logStatus(Notification notification, String status) {
        notification.setNotifStatus(status);
        notificationRepository.save(notification);
    }
}
