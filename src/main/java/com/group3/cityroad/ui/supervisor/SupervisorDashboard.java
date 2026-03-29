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
            new Button("View New Requests"),
            new Button("Assess Request"),
            new Button("Update Progress"),
            new Button("View Schedule")
        );
        
        add(header, menu);
    }
}
