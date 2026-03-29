package com.group3.cityroad.ui;

import com.group3.cityroad.entity.User;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.repository.BranchOfficeRepository;
import com.group3.cityroad.service.AuthenticationService;
import com.group3.cityroad.session.SessionManager;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;
import java.util.Map;

@Route(value = "signup", layout = MainLayout.class)
@PageTitle("Sign Up | City Road")
public class SignupView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticationService authService;
    private final SessionManager sessionManager;

    public SignupView(AuthenticationService authService, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.authService = authService;
        
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2("Create Resident Account");

        TextField username = new TextField("Username");
        TextField name = new TextField("Full Name");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        TextField address = new TextField("Address");
        TextField phone = new TextField("Phone");
        TextField area = new TextField("Area");

        FormLayout form = new FormLayout(username, name, password, confirmPassword, address, phone, area);
        
        Span errorSpan = new Span();
        errorSpan.getStyle().set("color", "red");

        Button submitButton = new Button("Register", event -> {
            try {
                if (!password.getValue().equals(confirmPassword.getValue())) {
                    throw new IllegalArgumentException("Passwords do not match");
                }
                if (password.getValue().length() < 8) {
                    throw new IllegalArgumentException("Password must be at least 8 characters");
                }

                Map<String, String> extraFields = new HashMap<>();
                extraFields.put("address", address.getValue());
                extraFields.put("phone", phone.getValue());
                extraFields.put("area", area.getValue());
                
                authService.register(username.getValue(), name.getValue(), password.getValue(), RoleEnum.RESIDENT, extraFields);

                Notification.show("Registration successful!", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));

            } catch (Exception ex) {
                errorSpan.setText(ex.getMessage());
            }
        });

        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        VerticalLayout container = new VerticalLayout(title, form, errorSpan, submitButton);
        container.setAlignItems(Alignment.CENTER);
        container.setMaxWidth("400px");

        add(container);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (sessionManager.isAuthenticated()) {
            User user = sessionManager.getCurrentUser();
            String route = "";
            switch (user.getRole()) {
                case RESIDENT -> route = "resident";
                case SUPERVISOR -> route = "supervisor";
                case ADMINISTRATOR -> route = "admin";
                case MAYOR -> route = "mayor";
            }
            event.forwardTo(route);
        }
    }
}
