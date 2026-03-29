package com.group3.cityroad.ui;

import com.group3.cityroad.session.SessionManager;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    private final SessionManager sessionManager;

    public MainLayout(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("City Road Maintenance System");
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)")
            .set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        VerticalLayout layout = new VerticalLayout();
        
        if (sessionManager.isAuthenticated()) {
            Span userSpan = new Span("Welcome, " + sessionManager.getCurrentUser().getName());
            userSpan.getStyle().set("font-weight", "bold");
            
            Button logoutButton = new Button("Logout", e -> {
                sessionManager.logout();
                getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
            });
            
            layout.add(userSpan, logoutButton);
        } else {
            RouterLink loginLink = new RouterLink("Login", LoginView.class);
            RouterLink signupLink = new RouterLink("Sign Up", SignupView.class);
            layout.add(loginLink, signupLink);
        }
        
        addToDrawer(layout);
    }
}
