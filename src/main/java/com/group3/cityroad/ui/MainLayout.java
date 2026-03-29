package com.group3.cityroad.ui;

import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.entity.User;
import com.group3.cityroad.ui.admin.AdminDashboard;
import com.group3.cityroad.ui.mayor.MayorDashboard;
import com.group3.cityroad.ui.resident.ResidentDashboard;
import com.group3.cityroad.ui.supervisor.SupervisorDashboard;
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
        RouterLink cityInfoLink = new RouterLink("City Info", com.group3.cityroad.ui.publicinfo.CityInfoView.class);
        
        if (sessionManager.isAuthenticated()) {
            User user = sessionManager.getCurrentUser();
            Span userSpan = new Span("Welcome, " + user.getName());
            userSpan.getStyle().set("font-weight", "bold");
            
            Class<? extends com.vaadin.flow.component.Component> dashboardClass = ResidentDashboard.class;
            switch (user.getRole()) {
                case RESIDENT -> dashboardClass = ResidentDashboard.class;
                case SUPERVISOR -> dashboardClass = SupervisorDashboard.class;
                case ADMINISTRATOR -> dashboardClass = AdminDashboard.class;
                case MAYOR -> dashboardClass = MayorDashboard.class;
            }
            RouterLink dashboardLink = new RouterLink("Dashboard", dashboardClass);
            
            Button logoutButton = new Button("Logout", e -> {
                sessionManager.logout();
                getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
            });
            
            layout.add(cityInfoLink, dashboardLink, userSpan, logoutButton);
        } else {
            RouterLink loginLink = new RouterLink("Login", LoginView.class);
            RouterLink signupLink = new RouterLink("Sign Up", SignupView.class);
            layout.add(cityInfoLink, loginLink, signupLink);
        }
        
        addToDrawer(layout);
    }
}
