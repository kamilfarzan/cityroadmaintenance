package com.group3.cityroad.ui.resident;

import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "resident", layout = MainLayout.class)
@PageTitle("Resident Dashboard | City Road")
public class ResidentDashboard extends ProtectedView {

    public ResidentDashboard(SessionManager sessionManager) {
        super(sessionManager, RoleEnum.RESIDENT);

        H2 header = new H2("Resident Operations");
        
        HorizontalLayout menu = new HorizontalLayout(
            new Button("Browse City Info", e -> getUI().ifPresent(ui -> ui.navigate(com.group3.cityroad.ui.publicinfo.CityInfoView.class))),
            new Button("Submit Repair Request", e -> getUI().ifPresent(ui -> ui.navigate(SubmitRepairRequestView.class))),
            new Button("Track My Requests", e -> getUI().ifPresent(ui -> ui.navigate(MyRepairRequestsView.class))),
            new Button("Check Notifications", e -> getUI().ifPresent(ui -> ui.navigate(MyNotificationsView.class)))
        );
        
        add(header, menu);
    }
}
