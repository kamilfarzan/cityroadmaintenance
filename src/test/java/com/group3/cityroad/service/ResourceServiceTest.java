package com.group3.cityroad.service;

import com.group3.cityroad.entity.Machine;
import com.group3.cityroad.entity.Resource;
import com.group3.cityroad.repository.RepairRequestRepository;
import com.group3.cityroad.repository.RepairScheduleRepository;
import com.group3.cityroad.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private RepairScheduleRepository repairScheduleRepository;

    @Mock
    private RepairRequestRepository repairRequestRepository;

    @InjectMocks
    private ResourceService resourceService;

    @Test
    void testUpdateQuantity_NormalUpdateNoDeficit() {
        Resource mockRes = new Machine();
        mockRes.setQuantity(2);

        when(resourceRepository.findById(50L)).thenReturn(Optional.of(mockRes));

        resourceService.updateQuantity(50L, 10); // Standard restock

        verify(resourceRepository, times(1)).save(mockRes);
        verify(repairScheduleRepository, never()).findAll(); // Should definitively not execute crisis reversal queries!
    }

    @Test
    void testUpdateQuantity_CrisisDeficitTrigger() {
        Resource mockRes = new Machine();
        mockRes.setQuantity(4);

        when(resourceRepository.findById(60L)).thenReturn(Optional.of(mockRes));
        // Return empty so crisis logic loops and breaks out cleanly mathematically
        when(repairScheduleRepository.findAll()).thenReturn(Collections.emptyList()); 

        resourceService.updateQuantity(60L, -1); // Administrator explicitly breaks a physical unit creating deficit

        verify(resourceRepository, times(1)).save(mockRes);
        verify(repairScheduleRepository, times(1)).findAll(); // Enforce crisis triggers logic pipeline correctly mapped!
    }
}
