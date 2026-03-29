package com.group3.cityroad.ui.resident;

import com.group3.cityroad.entity.BranchOffice;
import com.group3.cityroad.entity.Resident;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.repository.BranchOfficeRepository;
import com.group3.cityroad.service.RepairRequestService;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "resident/submit-request", layout = MainLayout.class)
@PageTitle("Submit Repair | City Road")
public class SubmitRepairRequestView extends ProtectedView {

    private final RepairRequestService repairRequestService;
    private final BranchOfficeRepository branchOfficeRepository;

    public SubmitRepairRequestView(SessionManager sessionManager, 
                                   RepairRequestService repairRequestService,
                                   BranchOfficeRepository branchOfficeRepository) {
        super(sessionManager, RoleEnum.RESIDENT);
        this.repairRequestService = repairRequestService;
        this.branchOfficeRepository = branchOfficeRepository;

        setAlignItems(Alignment.CENTER);

        H2 header = new H2("Submit a Repair Request");

        TextField roadLocation = new TextField("Precise Road Location");
        roadLocation.setRequired(true);
        roadLocation.setWidthFull();

        TextArea description = new TextArea("Problem Description");
        description.setRequired(true);
        description.setWidthFull();

        ComboBox<String> areaJurisdiction = new ComboBox<>("City Area Jurisdiction");
        areaJurisdiction.setRequired(true);
        // Safely pull string areas from the database so routing logic never fails
        List<String> validAreas = branchOfficeRepository.findAll().stream()
                .map(BranchOffice::getAreaJurisdiction)
                .collect(Collectors.toList());
        areaJurisdiction.setItems(validAreas);

        FormLayout form = new FormLayout(roadLocation, areaJurisdiction, description);
        form.setColspan(description, 2);

        Span errorSpan = new Span();
        errorSpan.getStyle().set("color", "red");

        Button submitBtn = new Button("Submit Request", e -> {
            try {
                if (roadLocation.isEmpty() || description.isEmpty() || areaJurisdiction.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required.");
                }

                Resident currentResident = (Resident) sessionManager.getCurrentUser();
                
                repairRequestService.submitRequest(currentResident, 
                                                   roadLocation.getValue(), 
                                                   description.getValue(), 
                                                   areaJurisdiction.getValue());

                Notification.show("Repair request submitted successfully!", 4000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                roadLocation.clear();
                description.clear();
                areaJurisdiction.clear();
                errorSpan.setText("");

            } catch (Exception ex) {
                errorSpan.setText(ex.getMessage());
            }
        });
        
        submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        RouterLink backLink = new RouterLink("Back to Dashboard", ResidentDashboard.class);

        VerticalLayout layout = new VerticalLayout(header, form, errorSpan, submitBtn, backLink);
        layout.setMaxWidth("600px");
        layout.setAlignItems(Alignment.CENTER);
        
        add(layout);
    }
}
