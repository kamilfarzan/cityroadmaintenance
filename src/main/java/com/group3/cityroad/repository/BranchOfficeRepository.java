package com.group3.cityroad.repository;

import com.group3.cityroad.entity.BranchOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchOfficeRepository extends JpaRepository<BranchOffice, Long> {
    Optional<BranchOffice> findByAreaJurisdiction(String areaJurisdiction);
}
