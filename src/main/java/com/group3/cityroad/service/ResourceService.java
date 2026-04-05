package com.group3.cityroad.service;

import com.group3.cityroad.entity.Machine;
import com.group3.cityroad.entity.Manpower;
import com.group3.cityroad.entity.RawMaterial;
import com.group3.cityroad.entity.Resource;
import com.group3.cityroad.entity.ResourceRequirement;
import com.group3.cityroad.entity.RepairSchedule;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.RepairRequestRepository;
import com.group3.cityroad.repository.RepairScheduleRepository;
import com.group3.cityroad.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private RepairScheduleRepository repairScheduleRepository;

    @Autowired
    private RepairRequestRepository repairRequestRepository;

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
            if (newQuantity < 0) {
                enforceResourceDeficits(r);
            }
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

    private void enforceResourceDeficits(Resource changedResource) {
        // Crisis check to seize resources from active jobs back into the pool until resolved
        List<RepairSchedule> scheduledJobs = repairScheduleRepository.findAll().stream()
                .filter(s -> s.getScheduleStatus() == StatusEnum.SCHEDULED)
                .toList();

        for (RepairSchedule schedule : scheduledJobs) {
            ResourceRequirement req = schedule.getRepairRequest().getRoadAssessment().getResourceRequirement();
            if (req == null) continue;

            boolean affected = false;

            if (changedResource instanceof Manpower && ((Manpower) changedResource).getPersonnelType().equals(req.getPersonnelType())) {
                affected = true;
            } else if (changedResource instanceof Machine && ((Machine) changedResource).getMachineType().equals(req.getMachineType())) {
                affected = true;
            } else if (changedResource instanceof RawMaterial && ((RawMaterial) changedResource).getMaterialType().equals(req.getMaterialType())) {
                affected = true;
            }

            if (affected) {
                // Return all resources assigned to this job back to global pools
                List<Resource> inventory = resourceRepository.findAll();
                
                if (req.getPersonnelQuantity() > 0) {
                    inventory.stream()
                        .filter(r -> r instanceof Manpower && ((Manpower) r).getPersonnelType().equals(req.getPersonnelType()))
                        .findFirst()
                        .ifPresent(r -> { r.setQuantity(r.getQuantity() + req.getPersonnelQuantity()); resourceRepository.save(r); });
                }
                if (req.getMachineQuantity() > 0) {
                    inventory.stream()
                        .filter(r -> r instanceof Machine && ((Machine) r).getMachineType().equals(req.getMachineType()))
                        .findFirst()
                        .ifPresent(r -> { r.setQuantity(r.getQuantity() + req.getMachineQuantity()); resourceRepository.save(r); });
                }
                if (req.getMaterialQuantity() > 0) {
                    inventory.stream()
                        .filter(r -> r instanceof RawMaterial && ((RawMaterial) r).getMaterialType().equals(req.getMaterialType()))
                        .findFirst()
                        .ifPresent(r -> { r.setQuantity(r.getQuantity() + req.getMaterialQuantity()); resourceRepository.save(r); });
                }

                // Defer the schedule
                schedule.setScheduleStatus(StatusEnum.UNDER_REVIEW);
                schedule.setIsDeferred(true);
                schedule.getRepairRequest().setStatus(StatusEnum.UNDER_REVIEW);
                
                repairRequestRepository.save(schedule.getRepairRequest());
                repairScheduleRepository.save(schedule);

                // Stop stealing resources if the deficit has resolved
                Resource currentRes = resourceRepository.findById(changedResource.getResourceId()).orElse(null);
                if (currentRes != null && currentRes.getQuantity() >= 0) {
                    break;
                }
            }
        }
    }
}
