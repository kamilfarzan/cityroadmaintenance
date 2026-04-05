package com.group3.cityroad.ui.admin;

import com.group3.cityroad.entity.Resource;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.service.ResourceService;
import com.group3.cityroad.session.SessionManager;
import com.group3.cityroad.ui.MainLayout;
import com.group3.cityroad.ui.ProtectedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.List;

@Route(value = "admin/resources", layout = MainLayout.class)
@PageTitle("Manage Resources | City Road")
public class ManageResourcesView extends ProtectedView {

    private final ResourceService resourceService;
    private final Grid<Resource> grid = new Grid<>(Resource.class, false);

    public ManageResourcesView(SessionManager sessionManager, ResourceService resourceService) {
        super(sessionManager, RoleEnum.ADMINISTRATOR);
        this.resourceService = resourceService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        H2 header = new H2("City Operation Resources");

        setupGrid();

        RouterLink backLink = new RouterLink("Back to Dashboard", AdminDashboard.class);

        add(header, grid, backLink);
        refreshGrid();
    }

    private void setupGrid() {
        grid.addColumn(Resource::getResourceId).setHeader("ID").setWidth("100px").setFlexGrow(0);
        grid.addColumn(Resource::getName).setHeader("Resource Name").setAutoWidth(true);
        // Using class name to determine resource type
        grid.addColumn(r -> r.getClass().getSimpleName()).setHeader("Category").setAutoWidth(true);
        grid.addColumn(Resource::getQuantity).setHeader("Current Quantity").setAutoWidth(true);
        
        grid.addComponentColumn(r -> {
            Span availability = new Span(r.getAvailable() ? "Yes" : "Out of Stock");
            if (r.getAvailable()) {
                availability.getStyle().set("color", "var(--lumo-success-color)");
            } else {
                availability.getStyle().set("color", "var(--lumo-error-color)");
            }
            return availability;
        }).setHeader("Available").setAutoWidth(true);

        grid.addComponentColumn(resource -> {
            Button editBtn = new Button("Edit Quantity", e -> openEditDialog(resource));
            editBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            return editBtn;
        }).setHeader("Manage").setWidth("150px").setFlexGrow(0);
        
        grid.setMaxWidth("1000px");
    }

    private void refreshGrid() {
        List<Resource> allResources = resourceService.getStatus();
        grid.setItems(allResources);
    }

    private void openEditDialog(Resource resource) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Manage Quantity: " + resource.getName());

        IntegerField quantityField = new IntegerField("Set Total Assigned Quantity");
        quantityField.setStepButtonsVisible(true);
        quantityField.setValue(resource.getQuantity() != null ? resource.getQuantity() : 0);

        Button saveBtn = new Button("Apply Restock", e -> {
            Integer newQty = quantityField.getValue();
            if (newQty != null) {
                resourceService.updateQuantity(resource.getResourceId(), newQty);
                Notification.show("Quantity Updated to " + newQty, 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();
                refreshGrid();
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        VerticalLayout layout = new VerticalLayout(quantityField);
        HorizontalLayout footer = new HorizontalLayout(cancelBtn, saveBtn);

        dialog.add(layout);
        dialog.getFooter().add(footer);
        
        dialog.open();
    }
}
