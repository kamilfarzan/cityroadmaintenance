package com.group3.cityroad.ui.resident;

import com.group3.cityroad.entity.ProgressUpdate;
import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.Resident;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.service.RepairRequestService;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "resident/my-requests", layout = MainLayout.class)
@PageTitle("My Repair Requests | City Road")
public class MyRepairRequestsView extends ProtectedView {

    private final RepairRequestService repairRequestService;
    private final Grid<RepairRequest> grid = new Grid<>(RepairRequest.class, false);

    public MyRepairRequestsView(SessionManager sessionManager, RepairRequestService repairRequestService) {
        super(sessionManager, RoleEnum.RESIDENT);
        this.repairRequestService = repairRequestService;

        setAlignItems(Alignment.CENTER);
        setSizeFull();

        H2 header = new H2("My Submitted Repair Requests");

        setupGrid();

        RouterLink backLink = new RouterLink("Back to Dashboard", ResidentDashboard.class);

        add(header, grid, backLink);
        refreshGrid();
    }

    private void setupGrid() {
        grid.addColumn(RepairRequest::getRequestId).setHeader("Request ID").setWidth("100px").setFlexGrow(0);
        grid.addColumn(RepairRequest::getRoadLocation).setHeader("Location").setAutoWidth(true);
        grid.addColumn(RepairRequest::getSubmissionDate).setHeader("Submitted On").setAutoWidth(true);

        grid.addComponentColumn(request -> {
            Span badge = new Span(request.getStatus().name());
            badge.getStyle().set("padding", "3px 8px");
            badge.getStyle().set("border-radius", "10px");
            badge.getStyle().set("font-size", "12px");
            // Highlight color based on status
            switch (request.getStatus()) {
                case SUBMITTED -> badge.getStyle().set("background", "var(--lumo-primary-color-10pct)").set("color",
                        "var(--lumo-primary-color)");
                case IN_PROGRESS -> badge.getStyle().set("background", "var(--lumo-warning-color-10pct)").set("color",
                        "var(--lumo-warning-color)");
                case COMPLETED -> badge.getStyle().set("background", "var(--lumo-success-color-10pct)").set("color",
                        "var(--lumo-success-color)");
                default -> badge.getStyle().set("background", "var(--lumo-contrast-10pct)");
            }
            return badge;
        }).setHeader("Status").setAutoWidth(true);

        grid.addComponentColumn(request -> {
            Button viewBtn = new Button("View Progress", e -> showProgressDialog(request));
            return viewBtn;
        }).setHeader("Actions").setWidth("150px").setFlexGrow(0);

        grid.setMaxWidth("100%");
    }

    private void refreshGrid() {
        Resident resident = (Resident) sessionManager.getCurrentUser();
        List<RepairRequest> myRequests = repairRequestService.getMyRequests(resident);
        grid.setItems(myRequests);
    }

    private void showProgressDialog(RepairRequest request) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Progress Details - Request #" + request.getRequestId());

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<ProgressUpdate> updates = request.getProgressUpdates();
        if (updates == null || updates.isEmpty()) {
            content.add(new Span("No progress updates have been posted by the supervisor yet."));
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
