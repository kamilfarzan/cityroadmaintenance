package com.group3.cityroad.ui.mayor;

import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.service.ReportService;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Route("mayor/print")
@PageTitle("Print Export")
public class MayorPrintView extends ProtectedView implements HasUrlParameter<String> {

    private final ReportService reportService;
    private final VerticalLayout document = new VerticalLayout();

    public MayorPrintView(SessionManager sessionManager, ReportService reportService) {
        super(sessionManager, RoleEnum.MAYOR);
        this.reportService = reportService;
        
        // Remove background styling for clean print
        getStyle().set("background", "white");
        getStyle().set("color", "black");
        
        document.setWidth("100%");
        document.setMaxWidth("800px");
        add(document);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();
        
        if (!params.containsKey("type")) {
            document.add(new H2("Invalid Print Parameters"));
            return;
        }
        
        String type = params.get("type").get(0);
        
        // Report Header
        document.add(new H1("City Road Management Authority"));
        document.add(new H3("Executive Statistical Export"));
        document.add(new Paragraph("Generated on: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
        document.add(new Hr());
        
        // Output body bounds
        if (MayorReportsView.RPT_COMPLETED.equals(type)) {
            String start = params.get("start").get(0);
            String end = params.get("end").get(0);
            document.add(new H2("Category: " + type + " (" + start + " to " + end + ")"));
            
            List<RepairRequest> list = reportService.getCompletedRepairsByDate(LocalDate.parse(start), LocalDate.parse(end));
            document.add(new H3("Sum Total Projects Reached: " + list.size()));
            
            list.forEach(r -> {
                Paragraph p = new Paragraph("Job #" + r.getRequestId() + " -> " + r.getRoadLocation() + " (Filed: " + r.getSubmissionDate() + ")");
                document.add(p);
            });
            
        } else if (MayorReportsView.RPT_OUTSTANDING.equals(type)) {
            document.add(new H2("Category: " + type));
            List<RepairRequest> list = reportService.getOutstandingRepairs();
            document.add(new H3("Current Active Queue Velocity: " + list.size()));
            
            list.forEach(r -> {
                Paragraph p = new Paragraph("Job #" + r.getRequestId() + " [" + r.getStatus() + "] -> " + r.getRoadLocation());
                document.add(p);
            });
            
        } else if (MayorReportsView.RPT_RESOURCES.equals(type)) {
            document.add(new H2("Category: Global Resource Capitalization"));
            Map<String, Integer> usage = reportService.calculateResourceUtilization();
            
            usage.entrySet().forEach(e -> {
                Paragraph p = new Paragraph("- " + e.getKey() + " : " + e.getValue() + " Active Field Units Deployed");
                document.add(p);
            });
        }
        
        document.add(new Hr());
        document.add(new Paragraph("Official Mayoral Internal Documentation | End of Report"));

        // Execute JS auto-print payload so print window drops as soon as rendered
        UI.getCurrent().getPage().executeJs("setTimeout(() => { window.print(); }, 500);");
    }
}
