package com.group3.cityroad.service;

import com.group3.cityroad.entity.Machine;
import com.group3.cityroad.entity.Manpower;
import com.group3.cityroad.entity.RawMaterial;
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
        List<Resource> inventory = resourceRepository.findAll();

        if (reqs.getPersonnelQuantity() > 0 && reqs.getPersonnelType() != null) {
            boolean pAvail = inventory.stream()
                .filter(r -> r instanceof Manpower && ((Manpower) r).getPersonnelType().equals(reqs.getPersonnelType()))
                .anyMatch(r -> r.getQuantity() != null && r.getQuantity() >= reqs.getPersonnelQuantity());
            if (!pAvail) return false;
        }

        if (reqs.getMachineQuantity() > 0 && reqs.getMachineType() != null) {
            boolean mAvail = inventory.stream()
                .filter(r -> r instanceof Machine && ((Machine) r).getMachineType().equals(reqs.getMachineType()))
                .anyMatch(r -> r.getQuantity() != null && r.getQuantity() >= reqs.getMachineQuantity());
            if (!mAvail) return false;
        }

        if (reqs.getMaterialQuantity() > 0 && reqs.getMaterialType() != null) {
            boolean valAvail = inventory.stream()
                .filter(r -> r instanceof RawMaterial && ((RawMaterial) r).getMaterialType().equals(reqs.getMaterialType()))
                .anyMatch(r -> r.getQuantity() != null && r.getQuantity() >= reqs.getMaterialQuantity());
            if (!valAvail) return false;
        }

        return true;
    }

    public void allocateResources(ResourceRequirement reqs) {
        List<Resource> inventory = resourceRepository.findAll();

        if (reqs.getPersonnelQuantity() > 0 && reqs.getPersonnelType() != null) {
            inventory.stream()
                .filter(r -> r instanceof Manpower && ((Manpower) r).getPersonnelType().equals(reqs.getPersonnelType()))
                .findFirst()
                .ifPresent(r -> {
                    r.setQuantity(r.getQuantity() - reqs.getPersonnelQuantity());
                    resourceRepository.save(r);
                });
        }

        if (reqs.getMachineQuantity() > 0 && reqs.getMachineType() != null) {
            inventory.stream()
                .filter(r -> r instanceof Machine && ((Machine) r).getMachineType().equals(reqs.getMachineType()))
                .findFirst()
                .ifPresent(r -> {
                    r.setQuantity(r.getQuantity() - reqs.getMachineQuantity());
                    resourceRepository.save(r);
                });
        }

        if (reqs.getMaterialQuantity() > 0 && reqs.getMaterialType() != null) {
            inventory.stream()
                .filter(r -> r instanceof RawMaterial && ((RawMaterial) r).getMaterialType().equals(reqs.getMaterialType()))
                .findFirst()
                .ifPresent(r -> {
                    r.setQuantity(r.getQuantity() - reqs.getMaterialQuantity());
                    resourceRepository.save(r);
                });
        }
    }
}
