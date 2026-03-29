package com.group3.cityroad.repository;

import com.group3.cityroad.entity.CityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityServiceRepository extends JpaRepository<CityService, Long> {
    List<CityService> findByCategory(String category);
    List<CityService> findByServiceNameContainingIgnoreCase(String keyword);
}
