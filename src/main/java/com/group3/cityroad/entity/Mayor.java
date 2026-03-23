package com.group3.cityroad.entity;

import com.group3.cityroad.enums.ReportTypeEnum;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Mayor — can view statistical reports on repair activity.
 */
@Entity
@DiscriminatorValue("MAYOR")
public class Mayor extends User {

    @Column
    private String mayorId;

    // --- Constructors ---

    public Mayor() {}

    public Mayor(String username, String name, String passwordHash) {
        super(username, name, passwordHash);
    }

    // --- Methods (signatures from class diagram) ---

    public void viewReports() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Object> viewStatistics() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setDateRange(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public String getMayorId() { return mayorId; }
    public void setMayorId(String mayorId) { this.mayorId = mayorId; }
}