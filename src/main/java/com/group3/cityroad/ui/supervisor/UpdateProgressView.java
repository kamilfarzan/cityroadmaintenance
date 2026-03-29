package com.group3.cityroad.ui.supervisor;

import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.Supervisor;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.RepairRequestRepository;
import com.group3.cityroad.service.ProgressUpdateService;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.group3.cityroad.entity.ProgressUpdate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "supervisor/progress", layout = MainLayout.class)
@PageTitle("Update Progress | City Road")
public class UpdateProgressView extends ProtectedView {

    private final RepairRequestRepository repairRequestRepository;
    private final ProgressUpdateService progressUpdateService;
    private final Grid<RepairRequest> grid = new Grid<>(RepairRequest.class, false);

    public UpdateProgressView(SessionManager sessionManager, 
                              RepairRequestRepository repairRequestRepository,
                              ProgressUpdateService progressUpdateService) {
        super(sessionManager, RoleEnum.SUPERVISOR);
        this.repairRequestRepository = repairRequestRepository;
        this.progressUpdateService = progressUpdateService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        H2 header = new H2("Active Job Field Updates");

        setupGrid();

        RouterLink backLink = new RouterLink("Back to Dashboard", SupervisorDashboard.class);

        add(header, grid, backLink);
        refreshGrid();
    }

    private void setupGrid() {
        grid.addColumn(RepairRequest::getRequestId).setHeader("Request ID").setWidth("120px").setFlexGrow(0);
        grid.addColumn(RepairRequest::getRoadLocation).setHeader("Active Worksite").setAutoWidth(true);
        grid.addColumn(RepairRequest::getStatus).setHeader("Current Status").setAutoWidth(true);

        grid.addComponentColumn(request -> {
            HorizontalLayout hp = new HorizontalLayout();
            
            Button viewBtn = new Button("View Logs", e -> showProgressDialog(request));
            Button updateBtn = new Button("Post Field Update", e -> openUpdateDialog(request));
            updateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            hp.add(viewBtn, updateBtn);
            return hp;
        }).setHeader("Manage Actions").setWidth("300px").setFlexGrow(0);
        
        grid.setMaxWidth("100%");
    }

    private void refreshGrid() {
        Supervisor supervisor = (Supervisor) sessionManager.getCurrentUser();
        List<RepairRequest> activeJobs = new ArrayList<>();
        
        if (supervisor.getBranchOffice() != null) {
            // Fetch SCHEDULED and IN_PROGRESS
            activeJobs.addAll(repairRequestRepository.findByBranchOfficeAndStatus(supervisor.getBranchOffice(), StatusEnum.SCHEDULED));
            activeJobs.addAll(repairRequestRepository.findByBranchOfficeAndStatus(supervisor.getBranchOffice(), StatusEnum.IN_PROGRESS));
        }
        
        grid.setItems(activeJobs);
    }

    private void openUpdateDialog(RepairRequest request) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Field Update: Request #" + request.getRequestId());

        NumberField progressPct = new NumberField("Current Job % Complete");
        progressPct.setMin(0.0);
        progressPct.setMax(100.0);
        progressPct.setValue(50.0);
        progressPct.setStepButtonsVisible(true);

        ComboBox<StatusEnum> statusCombo = new ComboBox<>("Field Status");
        statusCombo.setItems(StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED, StatusEnum.CLOSED, StatusEnum.UNDER_REVIEW);
        statusCombo.setValue(StatusEnum.IN_PROGRESS);

        statusCombo.addValueChangeListener(event -> {
            if (event.getValue() == StatusEnum.COMPLETED) {
                progressPct.setValue(100.0);
                progressPct.setReadOnly(true);
            } else {
                progressPct.setReadOnly(false);
            }
        });

        TextArea noteField = new TextArea("Field Notes / Blockers");
        noteField.setWidthFull();
        noteField.setPlaceholder("e.g. Cleared debris, waiting on cement trunk...");

        Button saveBtn = new Button("Commit to Ledger", e -> {
            try {
                Supervisor currentSupervisor = (Supervisor) sessionManager.getCurrentUser();
                
                Float finalPct = progressPct.getValue() != null ? progressPct.getValue().floatValue() : 0.0f;
                // Double safe guard, if status is COMPLETED, enforce 100%
                if (statusCombo.getValue() == StatusEnum.COMPLETED) {
                    finalPct = 100.0f;
                } else if (finalPct > 100.0f) {
                    finalPct = 100.0f;
                } else if (finalPct < 0.0f) {
                    finalPct = 0.0f;
                }
                
                progressUpdateService.addUpdate(
                        request, 
                        String.valueOf(currentSupervisor.getUserId()), 
                        finalPct, 
                        noteField.getValue(), 
                        statusCombo.getValue()
                );

                Notification.show("Update Pushed to Ledger! Residents can now view it.", 5000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                
                dialog.close();
                refreshGrid();

            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        VerticalLayout layout = new VerticalLayout(progressPct, statusCombo, noteField);
        layout.setPadding(false);
        HorizontalLayout footer = new HorizontalLayout(cancelBtn, saveBtn);

        dialog.add(layout);
        dialog.getFooter().add(footer);
        
        dialog.open();
    }

    private void showProgressDialog(RepairRequest request) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Progress Ledger - Request #" + request.getRequestId());

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<ProgressUpdate> updates = request.getProgressUpdates();
        if (updates == null || updates.isEmpty()) {
            content.add(new Span("No progress updates have been posted yet."));
        } else {
            for (ProgressUpdate currentUpdate : updates) {
                VerticalLayout updateCard = new VerticalLayout();
                updateCard.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
                updateCard.getStyle().set("border-radius", "5px");
                updateCard.getStyle().set("margin-bottom", "10px");

                Span dateSpan = new Span(currentUpdate.getTimestamp().format(dtf));
                dateSpan.getStyle().set("font-weight", "bold");
                dateSpan.getStyle().set("color", "var(--lumo-secondary-text-color)");

                Span statusSpan = new Span("Job Note: " + currentUpdate.getStatusNote());
                Span completionSpan = new Span("Percentage: " + currentUpdate.getProgressPct() + "%");

                updateCard.add(dateSpan, statusSpan, completionSpan);
                content.add(updateCard);
            }
        }

        Button closeButton = new Button("Close", e -> dialog.close());
        dialog.getFooter().add(closeButton);
        dialog.add(content);

        dialog.open();
    }
}
