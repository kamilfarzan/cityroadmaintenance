package com.group3.cityroad.ui;

import com.group3.cityroad.entity.User;
import com.group3.cityroad.exception.InvalidCredentialsException;
import com.group3.cityroad.service.AuthenticationService;
import com.group3.cityroad.session.SessionManager;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@RouteAlias(value = "", layout = MainLayout.class)
@Route(value = "login", layout = MainLayout.class)
@PageTitle("Login | City Road")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionManager sessionManager;

    public LoginView(AuthenticationService authService, SessionManager sessionManager) {
        this.sessionManager = sessionManager;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginForm login = new LoginForm();
        Span errorMessage = new Span();
        errorMessage.getStyle().set("color", "red");

        login.addLoginListener(e -> {
            try {
                User user = authService.login(e.getUsername(), e.getPassword());
                sessionManager.login(user);

                getUI().ifPresent(ui -> {
                    String route = "";
                    switch (user.getRole()) {
                        case RESIDENT -> route = "/resident";
                        case SUPERVISOR -> route = "/supervisor";
                        case ADMINISTRATOR -> route = "/admin";
                        case MAYOR -> route = "/mayor";
                    }
                    ui.getPage().setLocation(route);
                });
            } catch (InvalidCredentialsException ex) {
                login.setError(true);
                errorMessage.setText(ex.getMessage());
            }
        });

        add(new H1("City Road System"), login, errorMessage);
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
