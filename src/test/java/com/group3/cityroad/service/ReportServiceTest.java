package com.group3.cityroad.service;

import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.RoadAssessment;
import com.group3.cityroad.entity.ResourceRequirement;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.RepairRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private RepairRequestRepository repairRequestRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void testCalculateResourceUtilization_SumsProperly() {
        RepairRequest r1 = new RepairRequest();
        RoadAssessment a1 = new RoadAssessment();
        ResourceRequirement req1 = new ResourceRequirement();
        req1.setMachineType("Excavator");
        req1.setMachineQuantity(2);
        a1.setResourceRequirement(req1);
        r1.setRoadAssessment(a1);

        RepairRequest r2 = new RepairRequest();
        RoadAssessment a2 = new RoadAssessment();
        ResourceRequirement req2 = new ResourceRequirement();
        req2.setMachineType("Excavator");
        req2.setMachineQuantity(5);
        req2.setPersonnelType("Engineer");
        req2.setPersonnelQuantity(3);
        a2.setResourceRequirement(req2);
        r2.setRoadAssessment(a2);

        when(repairRequestRepository.findByStatusIn(anyList())).thenReturn(List.of(r1, r2));

        Map<String, Integer> usage = reportService.calculateResourceUtilization();

        assertEquals(2, usage.size(), "Should only track distinct requested classes");
        assertEquals(7, usage.get("Excavator"));
        assertEquals(3, usage.get("Engineer"));
    }
}
