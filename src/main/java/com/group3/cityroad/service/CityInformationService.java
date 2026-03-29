package com.group3.cityroad.service;

import com.group3.cityroad.entity.CityService;
import com.group3.cityroad.repository.CityServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityInformationService {

    @Autowired
    private CityServiceRepository cityServiceRepository;

    public List<String> getAllCategories() {
        return cityServiceRepository.findAll().stream()
                .map(CityService::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<CityService> getAllServices() {
        return cityServiceRepository.findAll();
    }

    public List<CityService> findByCategory(String category) {
        return cityServiceRepository.findByCategory(category);
    }

    public List<CityService> findByKeyword(String keyword) {
        return cityServiceRepository.findByServiceNameContainingIgnoreCase(keyword);
    }
}
