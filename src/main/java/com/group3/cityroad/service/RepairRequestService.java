package com.group3.cityroad.service;

import com.group3.cityroad.entity.BranchOffice;
import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.Resident;
import com.group3.cityroad.repository.BranchOfficeRepository;
import com.group3.cityroad.repository.RepairRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RepairRequestService {

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @Autowired
    private BranchOfficeRepository branchOfficeRepository;

    @Autowired
    private NotificationService notificationService;

    public RepairRequest submitRequest(Resident resident, String roadLocation, String description, String areaJurisdiction) {
        RepairRequest request = new RepairRequest(roadLocation, description, areaJurisdiction, resident);
        request.setResidentId(String.valueOf(resident.getUserId()));
        
        Optional<BranchOffice> branchOpt = branchOfficeRepository.findByAreaJurisdiction(areaJurisdiction);
        branchOpt.ifPresent(request::setBranchOffice);

        RepairRequest savedRequest = repairRequestRepository.save(request);
        notificationService.sendNotification(resident, "Your repair request has been submitted successfully.", com.group3.cityroad.enums.NotifTypeEnum.REQUEST_SUBMITTED);
        
        return savedRequest;
    }

    public List<RepairRequest> getMyRequests(Resident resident) {
        return repairRequestRepository.findByResident(resident);
    }

    public Optional<RepairRequest> getRequestDetails(Long requestId) {
        return repairRequestRepository.findById(requestId);
    }
}
