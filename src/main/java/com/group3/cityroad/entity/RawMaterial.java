package com.group3.cityroad.entity;

import jakarta.persistence.*;

/**
 * RawMaterial — a specific type of Resource representing consumable materials.
 * Examples: asphalt, gravel, concrete, paint, sand.
 */
@Entity
@DiscriminatorValue("RAW_MATERIAL")
public class RawMaterial extends Resource {

    @Column
    private String materialType;     // e.g. "Asphalt", "Gravel", "Concrete", "Paint"

    @Column
    private Float stock;             // quantity in stock (tonnes, litres, bags — depends on type)

    @Column
    private Float reorderLevel;      // alert admin when stock falls below this level

    // --- Constructors ---

    public RawMaterial() {}

    public RawMaterial(String name, String materialType, Integer quantity, Float stock) {
        super(name, quantity);
        this.materialType = materialType;
        this.stock = stock;
    }

    // --- Methods (signatures from class diagram) ---

    public void restock(Float amount) {
        if (amount != null && amount > 0) {
            this.stock = (this.stock != null ? this.stock : 0f) + amount;
        }
    }

    public boolean getLowStock() {
        if (stock == null || reorderLevel == null) return false;
        return stock <= reorderLevel;
    }

    @Override
    public float getUtilization() {
        // for materials, utilization = how much of original stock has been consumed
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // --- Getters and Setters ---

    public String getMaterialType() { return materialType; }
    public void setMaterialType(String materialType) { this.materialType = materialType; }

    public Float getStock() { return stock; }
    public void setStock(Float stock) { this.stock = stock; }

    public Float getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(Float reorderLevel) { this.reorderLevel = reorderLevel; }
}