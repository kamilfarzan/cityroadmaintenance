package com.group3.cityroad.ui.supervisor;

import com.group3.cityroad.entity.RepairRequest;
import com.group3.cityroad.entity.ResourceRequirement;
import com.group3.cityroad.entity.RoadAssessment;
import com.group3.cityroad.entity.Supervisor;
import com.group3.cityroad.enums.LocalityTypeEnum;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.enums.SeverityEnum;
import com.group3.cityroad.enums.StatusEnum;
import com.group3.cityroad.repository.RepairRequestRepository;
import com.group3.cityroad.service.AssessmentService;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.List;

@Route(value = "supervisor/assessments", layout = MainLayout.class)
@PageTitle("Assess Requests | City Road")
public class AssessRequestsView extends ProtectedView {

    private final RepairRequestRepository repairRequestRepository;
    private final AssessmentService assessmentService;
    private final Grid<RepairRequest> grid = new Grid<>(RepairRequest.class, false);

    public AssessRequestsView(SessionManager sessionManager, 
                              RepairRequestRepository repairRequestRepository,
                              AssessmentService assessmentService) {
        super(sessionManager, RoleEnum.SUPERVISOR);
        this.repairRequestRepository = repairRequestRepository;
        this.assessmentService = assessmentService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        H2 header = new H2("Unassessed Repair Requests");

        setupGrid();

        RouterLink backLink = new RouterLink("Back to Dashboard", SupervisorDashboard.class);

        add(header, grid, backLink);
        refreshGrid();
    }

    private void setupGrid() {
        grid.addColumn(RepairRequest::getRequestId).setHeader("ID").setWidth("100px").setFlexGrow(0);
        grid.addColumn(RepairRequest::getRoadLocation).setHeader("Location").setAutoWidth(true);
        grid.addColumn(RepairRequest::getDescription).setHeader("Description").setAutoWidth(true);
        grid.addColumn(RepairRequest::getSubmissionDate).setHeader("Submitted").setAutoWidth(true);

        grid.addComponentColumn(request -> {
            Button assessBtn = new Button("Assess", e -> openAssessmentDialog(request));
            assessBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            return assessBtn;
        }).setHeader("Action").setWidth("120px").setFlexGrow(0);
        
        grid.setMaxWidth("1000px");
    }

    private void refreshGrid() {
        Supervisor supervisor = (Supervisor) sessionManager.getCurrentUser();
        if (supervisor.getBranchOffice() != null) {
            List<RepairRequest> unassessed = repairRequestRepository.findByBranchOfficeAndStatus(supervisor.getBranchOffice(), StatusEnum.SUBMITTED);
            grid.setItems(unassessed);
        }
    }

    private void openAssessmentDialog(RepairRequest request) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Assess Request #" + request.getRequestId());

        ComboBox<SeverityEnum> severityCombo = new ComboBox<>("Damage Severity");
        severityCombo.setItems(SeverityEnum.values());
        severityCombo.setRequired(true);

        ComboBox<LocalityTypeEnum> localityCombo = new ComboBox<>("Locality Type");
        localityCombo.setItems(LocalityTypeEnum.values());
        localityCombo.setRequired(true);

        DatePicker startDate = new DatePicker("Proposed Start Date");
        DatePicker endDate = new DatePicker("Proposed End Date");

        ComboBox<String> personnelType = new ComboBox<>("Required Personnel Type");
        personnelType.setItems("Engineers", "Labourers", "Operators", "Inspector", "Safety Officer", "Others");
        IntegerField personnelQty = new IntegerField("Personnel Quantity");
        personnelQty.setMin(0);
        personnelQty.setValue(0);
        personnelQty.setStepButtonsVisible(true);

        ComboBox<String> machineType = new ComboBox<>("Required Machine");
        machineType.setItems("Excavators", "Rollers", "Mixer Truck", "Water Tanker", "Sprayer", "Jackhammer", "Sealer", "Others");
        IntegerField machineQty = new IntegerField("Machine Quantity");
        machineQty.setMin(0);
        machineQty.setValue(0);
        machineQty.setStepButtonsVisible(true);

        ComboBox<String> materialType = new ComboBox<>("Required Material");
        materialType.setItems("Asphalt", "Cement", "Paint", "Adhesives", "Others");
        IntegerField materialQty = new IntegerField("Material Quantity");
        materialQty.setMin(0);
        materialQty.setValue(0);
        materialQty.setStepButtonsVisible(true);

        Span errorSpan = new Span();
        errorSpan.getStyle().set("color", "red");

        Button submitBtn = new Button("Submit Assessment & Schedule", e -> {
            if (severityCombo.isEmpty() || localityCombo.isEmpty()) {
                errorSpan.setText("Please fill out severity and locality.");
                return;
            }

            try {
                Supervisor currentSupervisor = (Supervisor) sessionManager.getCurrentUser();
                
                RoadAssessment newAssessment = assessmentService.createAssessment(
                        request, 
                        String.valueOf(currentSupervisor.getUserId()), 
                        severityCombo.getValue(), 
                        localityCombo.getValue()
                );

                ResourceRequirement reqs = new ResourceRequirement(
                        newAssessment, 
                        personnelQty.getValue(), 
                        personnelType.getValue(), 
                        machineQty.getValue(), 
                        machineType.getValue(),
                        materialQty.getValue(),
                        materialType.getValue()
                );

                assessmentService.submitAssessment(newAssessment, reqs, startDate.getValue(), endDate.getValue());

                Notification.show("Assessment Processed. Routing to Scheduling logic...", 4000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        
                dialog.close();
                refreshGrid();

            } catch (Exception ex) {
                errorSpan.setText("Error processing assessment: " + ex.getMessage());
            }
        });

        submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        VerticalLayout layout = new VerticalLayout(
            severityCombo, localityCombo, 
            new HorizontalLayout(startDate, endDate),
            new HorizontalLayout(personnelType, personnelQty),
            new HorizontalLayout(machineType, machineQty),
            new HorizontalLayout(materialType, materialQty),
            errorSpan
        );
        dialog.add(layout);
        dialog.getFooter().add(cancelBtn, submitBtn);

        dialog.open();
    }
}
