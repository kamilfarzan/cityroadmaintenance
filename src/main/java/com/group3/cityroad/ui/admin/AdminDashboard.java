package com.group3.cityroad.ui.admin;

import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin Dashboard | City Road")
public class AdminDashboard extends ProtectedView {

    public AdminDashboard(SessionManager sessionManager) {
        super(sessionManager, RoleEnum.ADMINISTRATOR);

        H2 header = new H2("Administrator Operations");
        
        HorizontalLayout menu = new HorizontalLayout(
            new Button("Manage Users", e -> getUI().ifPresent(ui -> ui.navigate(AdminUserManagementView.class))),
            new Button("Manage Resources", e -> getUI().ifPresent(ui -> ui.navigate(ManageResourcesView.class)))
        );
        
        add(header, menu);
    }
}
