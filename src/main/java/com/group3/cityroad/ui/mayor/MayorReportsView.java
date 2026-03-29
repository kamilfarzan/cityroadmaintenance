package com.group3.cityroad.ui.mayor;

import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.service.ReportService;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Route(value = "mayor/reports", layout = MainLayout.class)
@PageTitle("Generate Reports | City Road")
public class MayorReportsView extends ProtectedView {

    private final ReportService reportService;

    // Report Type Selections
    public static final String RPT_COMPLETED = "Repairs Completed in Time Frame";
    public static final String RPT_OUTSTANDING = "Outstanding Repair Network";
    public static final String RPT_RESOURCES = "Resource Utilization Summation";

    private final VerticalLayout resultsLayout = new VerticalLayout();

    public MayorReportsView(SessionManager sessionManager, ReportService reportService) {
        super(sessionManager, RoleEnum.MAYOR);
        this.reportService = reportService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        H2 header = new H2("Statistical Analytics Platform");

        ComboBox<String> reportType = new ComboBox<>("Target Statistical Report");
        reportType.setItems(RPT_COMPLETED, RPT_OUTSTANDING, RPT_RESOURCES);
        reportType.setWidth("350px");

        DatePicker startDate = new DatePicker("Boundary Start Date");
        DatePicker endDate = new DatePicker("Boundary End Date");
        
        startDate.setVisible(false);
        endDate.setVisible(false);
        
        reportType.addValueChangeListener(e -> {
            boolean isTimeBounded = RPT_COMPLETED.equals(e.getValue());
            startDate.setVisible(isTimeBounded);
            endDate.setVisible(isTimeBounded);
        });

        Button generateBtn = new Button("Run Statistical Query", e -> {
            String selected = reportType.getValue();
            if (selected == null) {
                Notification.show("Please select a report type first.");
                return;
            }
            if (RPT_COMPLETED.equals(selected) && (startDate.getValue() == null || endDate.getValue() == null)) {
                Notification.show("Please select both a start and end date for bounded reports.");
                return;
            }
            runQuery(selected, startDate.getValue(), endDate.getValue());
        });
        generateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button openPrintBtn = new Button("Export as PDF / Print", e -> {
            String selected = reportType.getValue();
            if (selected == null) return;
            
            String url = "mayor/print?type=" + selected.replace(" ", "%20");
            if (startDate.getValue() != null && endDate.getValue() != null) {
                url += "&start=" + startDate.getValue().toString() + "&end=" + endDate.getValue().toString();
            }
            
            // Open new tab to pure Print view
            UI.getCurrent().getPage().open(url, "_blank");
        });

        HorizontalLayout form = new HorizontalLayout(reportType, startDate, endDate, generateBtn, openPrintBtn);
        form.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        resultsLayout.setWidthFull();
        resultsLayout.setMaxWidth("1000px");

        RouterLink backLink = new RouterLink("Return to Dashboard", MayorDashboard.class);

        add(header, form, resultsLayout, backLink);
    }

    private void runQuery(String type, LocalDate start, LocalDate end) {
        resultsLayout.removeAll();
        
        if (RPT_COMPLETED.equals(type)) {
            List<RepairRequest> list = reportService.getCompletedRepairsByDate(start, end);
            resultsLayout.add(new H2("Results: " + list.size() + " Total Projects Concluded"));
            
            Grid<RepairRequest> grid = new Grid<>(RepairRequest.class, false);
            grid.addColumn(RepairRequest::getRequestId).setHeader("Job ID");
            grid.addColumn(RepairRequest::getRoadLocation).setHeader("Location");
            grid.addColumn(RepairRequest::getSubmissionDate).setHeader("Initial Submission");
            grid.setItems(list);
            resultsLayout.add(grid);

        } else if (RPT_OUTSTANDING.equals(type)) {
            List<RepairRequest> list = reportService.getOutstandingRepairs();
            resultsLayout.add(new H2("Results: " + list.size() + " Active Field Obligations"));

            Grid<RepairRequest> grid = new Grid<>(RepairRequest.class, false);
            grid.addColumn(RepairRequest::getRequestId).setHeader("Job ID").setFlexGrow(0);
            grid.addColumn(RepairRequest::getStatus).setHeader("Status");
            grid.addColumn(RepairRequest::getRoadLocation).setHeader("Location");
            grid.setItems(list);
            resultsLayout.add(grid);

        } else if (RPT_RESOURCES.equals(type)) {
            Map<String, Integer> usage = reportService.calculateResourceUtilization();
            resultsLayout.add(new H2("Total Volume Resource Consumption (Active & Complete)"));
            
            VerticalLayout stack = new VerticalLayout();
            stack.getStyle().set("border", "1px solid var(--lumo-contrast-10pct)");
            stack.getStyle().set("border-radius", "8px");
            stack.setPadding(true);
            
            for (Map.Entry<String, Integer> entry : usage.entrySet()) {
                Span line = new Span(entry.getKey() + " : " + entry.getValue() + " units actively provisioned");
                line.getStyle().set("font-size", "1.1em");
                stack.add(line);
            }
            if (usage.isEmpty()) stack.add(new Span("No active resources consumed."));
            
            resultsLayout.add(stack);
        }
    }
}
