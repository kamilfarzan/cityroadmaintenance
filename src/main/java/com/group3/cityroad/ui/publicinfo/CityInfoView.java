package com.group3.cityroad.ui.publicinfo;

import com.group3.cityroad.entity.CityService;
import com.group3.cityroad.service.CityInformationService;
import com.group3.cityroad.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "city-info", layout = MainLayout.class)
@PageTitle("City Services Information | City Road")
public class CityInfoView extends VerticalLayout {

    private final CityInformationService cityInfoService;
    private final Grid<CityService> grid = new Grid<>(CityService.class);
    private final TextField filterText = new TextField();

    public CityInfoView(CityInformationService cityInfoService) {
        this.cityInfoService = cityInfoService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        H2 header = new H2("City Facility & Services Hub");

        filterText.setPlaceholder("Search by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        configureGrid();

        add(header, filterText, grid);
        updateList();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setMaxWidth("100%");
        grid.setColumns("serviceName", "category", "description", "location", "contactInfo", "timing");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        if (filterText.getValue().isEmpty()) {
            grid.setItems(cityInfoService.getAllServices());
        } else {
            grid.setItems(cityInfoService.findByKeyword(filterText.getValue()));
        }
    }
}
