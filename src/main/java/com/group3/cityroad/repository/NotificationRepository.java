package com.group3.cityroad.repository;

import com.group3.cityroad.entity.Notification;
import com.group3.cityroad.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByResidentAndNotifStatus(Resident resident, String notifStatus);
    List<Notification> findByNotifStatus(String notifStatus);
}
