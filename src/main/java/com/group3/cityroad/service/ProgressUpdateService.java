package com.group3.cityroad.service;

import com.group3.cityroad.entity.ProgressUpdate;
import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.enums.NotifTypeEnum;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.ProgressUpdateRepository;
import com.group3.cityroad.repository.RepairRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgressUpdateService {

    @Autowired
    private ProgressUpdateRepository progressUpdateRepository;

    @Autowired
    private RepairRequestRepository repairRequestRepository;
    
    @Autowired
    private NotificationService notificationService;

    public ProgressUpdate addUpdate(RepairRequest request, String supervisorId, Float pct, String note, StatusEnum statusFlag) {
        ProgressUpdate update = new ProgressUpdate();
        update.setRepairRequest(request);
        update.setRequestId(String.valueOf(request.getRequestId()));
        update.setSupervisorId(supervisorId);
        update.setProgressPct(pct);
        update.setStatusNote(note);
        update.setStatusFlag(statusFlag);
        update.setTimestamp(LocalDateTime.now());
        
        // Sync the passed field status directly to the Request and Schedule
        request.setStatus(statusFlag);
        if (request.getRepairSchedule() != null) {
            request.getRepairSchedule().setScheduleStatus(statusFlag);
        }

        if (pct >= 100f || statusFlag == StatusEnum.COMPLETED) {
            update.setIsComplete(true);
            markComplete(request);
        } else {
            // Save the synchronized in-progress/other statuses directly
            repairRequestRepository.save(request);
        }
        
        return progressUpdateRepository.save(update);
    }

    public void markComplete(RepairRequest request) {
        request.setStatus(StatusEnum.COMPLETED);
        if (request.getRepairSchedule() != null) {
            request.getRepairSchedule().setScheduleStatus(StatusEnum.COMPLETED);
        }
        repairRequestRepository.save(request);
        
        if(request.getResident() != null) {
            notificationService.sendNotification(request.getResident(), "Your road repair request has been completed.", NotifTypeEnum.REPAIR_COMPLETED);
        }
    }

    public List<ProgressUpdate> getHistory(RepairRequest request) {
        return progressUpdateRepository.findByRepairRequestOrderByTimestampDesc(request);
    }
}
