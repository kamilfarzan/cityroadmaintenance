package com.group3.cityroad.ui.supervisor;

import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "supervisor", layout = MainLayout.class)
@PageTitle("Supervisor Dashboard | City Road")
public class SupervisorDashboard extends ProtectedView {

    public SupervisorDashboard(SessionManager sessionManager) {
        super(sessionManager, RoleEnum.SUPERVISOR);

        H2 header = new H2("Supervisor Operations");
        
        HorizontalLayout menu = new HorizontalLayout(
            new Button("Assess Requests", e -> getUI().ifPresent(ui -> ui.navigate(AssessRequestsView.class))),
            new Button("Update Progress", e -> getUI().ifPresent(ui -> ui.navigate(UpdateProgressView.class))),
            new Button("View Schedules", e -> getUI().ifPresent(ui -> ui.navigate(SupervisorSchedulesView.class)))
        );
        
        add(header, menu);
    }
}
