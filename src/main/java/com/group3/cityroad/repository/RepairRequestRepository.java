package com.group3.cityroad.repository;

import com.group3.cityroad.entity.BranchOffice;
import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.Resident;
import com.group3.cityroad.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    List<RepairRequest> findByBranchOfficeAndStatus(BranchOffice branchOffice, StatusEnum status);
    List<RepairRequest> findByResident(Resident resident);
    List<RepairRequest> findBySubmissionDateBetween(LocalDate startDate, LocalDate endDate);
}
