package com.group3.cityroad.service;

import com.group3.cityroad.dto.ScheduleResultDTO;
import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.RoadAssessment;
import com.group3.cityroad.entity.ResourceRequirement;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.RepairRequestRepository;
import com.group3.cityroad.repository.RepairScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock
    private RepairScheduleRepository repairScheduleRepository;

    @Mock
    private RepairRequestRepository repairRequestRepository;

    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private SchedulingService schedulingService;

    private RoadAssessment mockAssessment;
    private RepairRequest mockRequest;
    private ResourceRequirement mockReqs;

    @BeforeEach
    void setUp() {
        mockRequest = new RepairRequest();
        mockRequest.setStatus(StatusEnum.SUBMITTED);

        mockReqs = new ResourceRequirement();
        mockAssessment = new RoadAssessment();
        mockAssessment.setRepairRequest(mockRequest);
        mockAssessment.setResourceRequirement(mockReqs);
    }

    @Test
    void testScheduleRepair_SuccessResourceAvailable() {
        when(resourceService.checkAvailability(mockReqs)).thenReturn(true);
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(2);

        ScheduleResultDTO result = schedulingService.scheduleRepair(mockAssessment, start, end);

        assertTrue(result.isSuccessful());
        assertEquals(StatusEnum.SCHEDULED, result.getSchedule().getScheduleStatus());
        assertEquals(StatusEnum.SCHEDULED, mockRequest.getStatus(), "Parent request should be updated mapping state cascade");
        
        verify(repairScheduleRepository, times(1)).save(any());
        verify(resourceService, times(1)).allocateResources(mockReqs); // Confirms physically seizing inventory limits
    }

    @Test
    void testScheduleRepair_FailureResourceDeficit() {
        when(resourceService.checkAvailability(mockReqs)).thenReturn(false);

        ScheduleResultDTO result = schedulingService.scheduleRepair(mockAssessment, null, null);

        assertFalse(result.isSuccessful(), "Scheduling should fail if resources are depleted");
        assertEquals(StatusEnum.UNDER_REVIEW, result.getSchedule().getScheduleStatus(), "Schedules must freeze to Under Review status");
        
        verify(repairScheduleRepository, times(1)).save(any());
        verify(resourceService, never()).allocateResources(any()); // Should not deduct ghost stock
    }
}
