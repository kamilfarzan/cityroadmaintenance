package com.group3.cityroad.session;

import com.group3.cityroad.entity.User;
import com.group3.cityroad.enums.RoleEnum;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@VaadinSessionScope
public class SessionManager implements Serializable {

    private User currentUser;

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean hasRole(RoleEnum role) {
        return isAuthenticated() && currentUser.getRole() == role;
    }
}
