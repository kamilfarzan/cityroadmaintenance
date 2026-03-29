package com.group3.cityroad.ui;

import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.session.SessionManager;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class ProtectedView extends VerticalLayout implements BeforeEnterObserver {

    protected final SessionManager sessionManager;
    private final RoleEnum requiredRole;

    public ProtectedView(SessionManager sessionManager, RoleEnum requiredRole) {
        this.sessionManager = sessionManager;
        this.requiredRole = requiredRole;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionManager.isAuthenticated()) {
            event.forwardTo(LoginView.class);
            return;
        }

        if (requiredRole != null && !sessionManager.hasRole(requiredRole)) {
            event.forwardTo(LoginView.class);
        }
    }
}
