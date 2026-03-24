package com.group3.cityroad.entity;

import jakarta.persistence.*;

/**
 * Resource — base entity for all resource types.
 * Single-table inheritance: Manpower, Machine, RawMaterial share the "resource" table.
 * Discriminator column is "resource_type".
 */
@Entity
@Table(name = "resource")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "resource_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer quantity;

    @Column
    private Boolean available;

    // --- Constructors ---

    protected Resource() {}

    protected Resource(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
        this.available = quantity > 0;
    }

    // --- Methods (signatures from class diagram) ---

    public void checkAvailability() {
        this.available = (this.quantity != null && this.quantity > 0);
    }

    public void updateQuantity(Integer newQty) {
        this.quantity = newQty;
        this.available = newQty > 0;
    }

    public float getUtilization() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public Long getResourceId() { return resourceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
}