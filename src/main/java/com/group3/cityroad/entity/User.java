package com.group3.cityroad.entity;

import com.group3.cityroad.enums.RoleEnum;
import jakarta.persistence.*;

/**
 * Base entity for all user types.
 * Single-table inheritance: all subtypes share the "users" table.
 * The "role" column acts as the discriminator.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable = false, updatable = false)
    private RoleEnum role;

    // --- Constructors ---

    protected User() {}

    protected User(String username, String name, String passwordHash) {
        this.username = username;
        this.name = name;
        this.passwordHash = passwordHash;
    }

    // --- Methods (signatures from class diagram) ---

    public boolean login(String username, String password) {
        // implemented in AuthenticationService
        throw new UnsupportedOperationException("Use AuthenticationService.login()");
    }

    public void logout() {
        // implemented in AuthenticationService
        throw new UnsupportedOperationException("Use AuthenticationService.revokeSession()");
    }

    // --- Getters and Setters ---

    public Long getUserId() { return userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public RoleEnum getRole() { return role; }
}