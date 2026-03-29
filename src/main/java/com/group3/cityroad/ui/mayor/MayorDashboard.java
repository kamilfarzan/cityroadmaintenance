package com.group3.cityroad.ui.mayor;

import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "mayor", layout = MainLayout.class)
@PageTitle("Mayor Dashboard | City Road")
public class MayorDashboard extends ProtectedView {

    public MayorDashboard(SessionManager sessionManager) {
        super(sessionManager, RoleEnum.MAYOR);

        H2 header = new H2("Mayor Operations");
        
        HorizontalLayout menu = new HorizontalLayout(
            new Button("Generate Reports")
        );
        
        add(header, menu);
    }
}
