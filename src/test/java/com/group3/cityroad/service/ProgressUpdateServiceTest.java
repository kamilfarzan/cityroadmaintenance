package com.group3.cityroad.service;

import com.group3.cityroad.entity.ProgressUpdate;
import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.Resident;
import com.group3.cityroad.enums.NotifTypeEnum;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.ProgressUpdateRepository;
import com.group3.cityroad.repository.RepairRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressUpdateServiceTest {

    @Mock
    private ProgressUpdateRepository progressUpdateRepository;

    @Mock
    private RepairRequestRepository repairRequestRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ProgressUpdateService progressUpdateService;

    private RepairRequest mockRequest;
    private Resident mockResident;

    @BeforeEach
    void setUp() {
        mockResident = new Resident();
        mockResident.setUsername("tester");

        mockRequest = new RepairRequest();
        mockRequest.setStatus(StatusEnum.IN_PROGRESS);
        mockRequest.setResident(mockResident);
    }

    @Test
    void testAddUpdate_MarksCompleteOn100Percent() {
        when(progressUpdateRepository.save(any(ProgressUpdate.class))).thenAnswer(i -> i.getArguments()[0]);

        ProgressUpdate update = progressUpdateService.addUpdate(mockRequest, "supervisor_2", 100.0f, "Job finished fully", StatusEnum.IN_PROGRESS);

        assertTrue(update.getIsComplete(), "The raw logic must securely flip completion based purely on 100% threshold constraint.");
        assertEquals(StatusEnum.COMPLETED, mockRequest.getStatus()); // Synchronized mapping check

        verify(notificationService, times(1)).sendNotification(mockResident, "Your road repair request has been completed.", NotifTypeEnum.REPAIR_COMPLETED);
    }

    @Test
    void testAddUpdate_StandardInProgress() {
        when(progressUpdateRepository.save(any(ProgressUpdate.class))).thenAnswer(i -> i.getArguments()[0]);

        ProgressUpdate update = progressUpdateService.addUpdate(mockRequest, "supervisor_3", 75.0f, "Doing fine", StatusEnum.IN_PROGRESS);

        assertNull(update.getIsComplete(), "Must definitively not trigger boolean closed flags simply during transit updates.");
        assertEquals(StatusEnum.IN_PROGRESS, mockRequest.getStatus());

        verify(notificationService, never()).sendNotification(any(), anyString(), any()); // Do not spam the user
    }
}
