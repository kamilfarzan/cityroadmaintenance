package com.group3.cityroad.ui.admin;

import com.group3.cityroad.entity.BranchOffice;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.repository.BranchOfficeRepository;
import com.group3.cityroad.service.AuthenticationService;
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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.HashMap;
import java.util.Map;

@Route(value = "admin/users", layout = MainLayout.class)
@PageTitle("Manage Users | City Road")
public class AdminUserManagementView extends ProtectedView {

    private final AuthenticationService authService;
    private final BranchOfficeRepository branchOfficeRepository;

    public AdminUserManagementView(SessionManager sessionManager, AuthenticationService authService,
                                   BranchOfficeRepository branchOfficeRepository) {
        super(sessionManager, RoleEnum.ADMINISTRATOR);
        this.authService = authService;
        this.branchOfficeRepository = branchOfficeRepository;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2("Create Privileged Account");

        TextField username = new TextField("Username");
        TextField name = new TextField("Full Name");
        PasswordField password = new PasswordField("Temporary Password");
        ComboBox<RoleEnum> roleSelect = new ComboBox<>("Role");
        roleSelect.setItems(RoleEnum.SUPERVISOR, RoleEnum.ADMINISTRATOR);
        roleSelect.setValue(RoleEnum.SUPERVISOR);

        FormLayout form = new FormLayout(username, name, password, roleSelect);

        ComboBox<BranchOffice> branchSelect = new ComboBox<>("Branch Office");
        branchSelect.setItems(branchOfficeRepository.findAll());
        branchSelect.setItemLabelGenerator(BranchOffice::getName);
        FormLayout supervisorForm = new FormLayout(branchSelect);

        VerticalLayout conditionalContainer = new VerticalLayout();
        conditionalContainer.setPadding(false);
        conditionalContainer.add(supervisorForm);

        roleSelect.addValueChangeListener(e -> {
            conditionalContainer.removeAll();
            if (e.getValue() == RoleEnum.SUPERVISOR) {
                conditionalContainer.add(supervisorForm);
            }
        });

        Span errorSpan = new Span();
        errorSpan.getStyle().set("color", "red");

        Button submitButton = new Button("Create User", event -> {
            try {
                if (password.getValue().length() < 8) {
                    throw new IllegalArgumentException("Password must be at least 8 characters");
                }

                Map<String, String> extraFields = new HashMap<>();
                if (roleSelect.getValue() == RoleEnum.SUPERVISOR) {
                    if (branchSelect.getValue() != null) {
                        extraFields.put("branchOfficeId", String.valueOf(branchSelect.getValue().getBranchId()));
                    } else {
                        throw new IllegalArgumentException("Supervisor must select a Branch Office.");
                    }
                }

                authService.register(username.getValue(), name.getValue(), password.getValue(), roleSelect.getValue(), extraFields);

                Notification.show("User " + username.getValue() + " created successfully!", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        
                // Clear the form
                username.clear();
                name.clear();
                password.clear();
                branchSelect.clear();

            } catch (Exception ex) {
                errorSpan.setText(ex.getMessage());
            }
        });

        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        RouterLink backLink = new RouterLink("Back to Dashboard", AdminDashboard.class);
        
        VerticalLayout container = new VerticalLayout(title, form, conditionalContainer, errorSpan, submitButton, backLink);
        container.setAlignItems(Alignment.CENTER);
        container.setMaxWidth("400px");

        add(container);
    }
}
