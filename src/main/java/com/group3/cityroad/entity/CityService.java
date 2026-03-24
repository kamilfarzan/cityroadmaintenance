package com.group3.cityroad.entity;

import jakarta.persistence.*;

/**
 * CityService — represents a city facility or service that residents can browse.
 * Corresponds to UC1: Access City Information.
 */
@Entity
@Table(name = "city_service")
public class CityService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(nullable = false)
    private String serviceName;

    @Column
    private String category;

    @Column(length = 1000)
    private String description;

    @Column
    private String location;

    @Column
    private String contactInfo;

    @Column
    private String timing;

    // --- Constructors ---

    public CityService() {}

    public CityService(String serviceName, String category, String description,
                       String location, String contactInfo) {
        this.serviceName = serviceName;
        this.category = category;
        this.description = description;
        this.location = location;
        this.contactInfo = contactInfo;
    }

    // --- Methods (signatures from class diagram) ---

    public void getInfo() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getByCategory() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void update() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public Long getServiceId() { return serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getTiming() { return timing; }
    public void setTiming(String timing) { this.timing = timing; }
}