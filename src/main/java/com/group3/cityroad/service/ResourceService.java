package com.group3.cityroad.service;

import com.group3.cityroad.entity.Resource;
import com.group3.cityroad.entity.ResourceRequirement;
import com.group3.cityroad.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> getStatus() {
        return resourceRepository.findAll();
    }

    public Resource addResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public void updateQuantity(Long resourceId, Integer newQuantity) {
        resourceRepository.findById(resourceId).ifPresent(r -> {
            r.setQuantity(newQuantity);
            resourceRepository.save(r);
        });
    }

    public void removeResource(Long resourceId) {
        resourceRepository.deleteById(resourceId);
    }

    public boolean checkAvailability(ResourceRequirement reqs) {
        // Simplified mock check. Assume available if requested quantity > 0 for now.
        // Needs proper query checking raw materials, machines, and personnel counts.
        return true;
    }
}
