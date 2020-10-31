package com.apollo.backend.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Person extends GenericEntity {

    @NotNull(message = "Name is mandatory")
    @Size(min = 1, max = 300, message = "Name must be between 1 and 300 characters")
    private String name;

    @NotNull(message = "Role is mandatory")
    @Size(min = 1, max = 300, message = "Role must be between 1 and 300 characters")
    private String role;

    protected Person() {
    }

    public Person(String name, String role) {
        this.setName(name);
        this.setRole(role);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}