package com.group3.cityroad.ui.supervisor;

import com.group3.cityroad.entity.RepairSchedule;
import com.group3.cityroad.entity.Supervisor;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.repository.RepairScheduleRepository;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.group3.cityroad.entity.ProgressUpdate;
import com.group3.cityroad.enums.StatusEnum;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "supervisor/schedules", layout = MainLayout.class)
@PageTitle("Job Schedules | City Road")
public class SupervisorSchedulesView extends ProtectedView {

    private final RepairScheduleRepository repairScheduleRepository;
    private final Grid<RepairSchedule> grid = new Grid<>(RepairSchedule.class, false);

    public SupervisorSchedulesView(SessionManager sessionManager, RepairScheduleRepository repairScheduleRepository) {
        super(sessionManager, RoleEnum.SUPERVISOR);
        this.repairScheduleRepository = repairScheduleRepository;

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        H2 header = new H2("Assigned Active Repair Schedules");

        setupGrid();

        RouterLink backLink = new RouterLink("Back to Dashboard", SupervisorDashboard.class);

        add(header, grid, backLink);
        refreshGrid();
    }

    private void setupGrid() {
        grid.addColumn(RepairSchedule::getScheduleId).setHeader("Schedule ID").setWidth("120px").setFlexGrow(0);
        grid.addColumn(RepairSchedule::getRequestId).setHeader("Assigned Repair ID").setAutoWidth(true);
        grid.addColumn(RepairSchedule::getPriority).setHeader("Priority Tier").setAutoWidth(true);
        grid.addColumn(RepairSchedule::getStartDate).setHeader("Proposed Start").setAutoWidth(true);
        grid.addColumn(RepairSchedule::getEndDate).setHeader("Proposed End").setAutoWidth(true);
        
        grid.addComponentColumn(schedule -> {
            StatusEnum currentStatus = schedule.getRepairRequest().getStatus();
            Span badge = new Span(currentStatus.name());
            badge.getStyle().set("padding", "3px 8px");
            badge.getStyle().set("border-radius", "10px");
            badge.getStyle().set("font-size", "12px");
            
            switch (currentStatus) {
                case SCHEDULED -> badge.getStyle().set("background", "var(--lumo-primary-color-10pct)").set("color", "var(--lumo-primary-color)");
                case IN_PROGRESS -> badge.getStyle().set("background", "var(--lumo-warning-color-10pct)").set("color", "var(--lumo-warning-color)");
                case COMPLETED -> badge.getStyle().set("background", "var(--lumo-success-color-10pct)").set("color", "var(--lumo-success-color)");
                default -> badge.getStyle().set("background", "var(--lumo-contrast-10pct)");
            }
            return badge;
        }).setHeader("Status").setAutoWidth(true);

        grid.addComponentColumn(schedule -> {
            var assessment = schedule.getRepairRequest().getRoadAssessment();
            if (assessment == null || assessment.getResourceRequirement() == null) return new Span("N/A");
            
            var req = assessment.getResourceRequirement();
            VerticalLayout vl = new VerticalLayout();
            vl.setPadding(false);
            vl.setSpacing(false);
            if (req.getPersonnelQuantity() > 0) vl.add(new Span(req.getPersonnelQuantity() + " " + req.getPersonnelType()));
            if (req.getMachineQuantity() > 0) vl.add(new Span(req.getMachineQuantity() + " " + req.getMachineType()));
            if (req.getMaterialQuantity() > 0) vl.add(new Span(req.getMaterialQuantity() + " " + req.getMaterialType()));
            return vl;
        }).setHeader("Allotted Resources").setAutoWidth(true);

        grid.addComponentColumn(schedule -> {
            Button viewBtn = new Button("View Logs", e -> showProgressDialog(schedule.getRepairRequest()));
            return viewBtn;
        }).setHeader("Manage").setWidth("150px").setFlexGrow(0);

        grid.setMaxWidth("100%");
    }

    private void refreshGrid() {
        Supervisor supervisor = (Supervisor) sessionManager.getCurrentUser();
        List<RepairSchedule> items = repairScheduleRepository.findBySupervisorId(String.valueOf(supervisor.getUserId()));
        grid.setItems(items);
    }

    private void showProgressDialog(com.group3.cityroad.entity.RepairRequest request) {
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
