package com.group3.cityroad.ui.resident;

import com.group3.cityroad.entity.Notification;
import com.group3.cityroad.entity.Resident;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.repository.NotificationRepository;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "resident/notifications", layout = MainLayout.class)
@PageTitle("My Notifications | City Road")
public class MyNotificationsView extends ProtectedView {

    private final NotificationRepository notificationRepository;

    public MyNotificationsView(SessionManager sessionManager, NotificationRepository notificationRepository) {
        super(sessionManager, RoleEnum.RESIDENT);
        this.notificationRepository = notificationRepository;

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        H2 header = new H2("My System Notifications");
        RouterLink backLink = new RouterLink("Back to Dashboard", ResidentDashboard.class);

        VerticalLayout listLayout = new VerticalLayout();
        listLayout.setWidthFull();
        listLayout.setMaxWidth("800px");

        Resident resident = (Resident) sessionManager.getCurrentUser();
        List<Notification> notifications = notificationRepository.findByResidentOrderByCreatedAtDesc(resident);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

        if (notifications.isEmpty()) {
            listLayout.add(new Paragraph("You currently have no unread or historical notifications."));
        } else {
            for (Notification notif : notifications) {
                VerticalLayout card = new VerticalLayout();
                card.getStyle().set("border", "1px solid var(--lumo-contrast-10pct)");
                card.getStyle().set("border-radius", "8px");
                card.getStyle().set("box-shadow", "var(--lumo-box-shadow-xs)");
                card.setPadding(true);

                Span dateHeader = new Span(notif.getCreatedAt().format(dtf) + " | Type: " + notif.getType().name());
                dateHeader.getStyle().set("font-size", "0.9em");
                dateHeader.getStyle().set("color", "var(--lumo-secondary-text-color)");

                Span message = new Span(notif.getMessage());
                message.getStyle().set("font-weight", "500");

                card.add(dateHeader, message);
                listLayout.add(card);
            }
        }

        add(header, listLayout, backLink);
    }
}
