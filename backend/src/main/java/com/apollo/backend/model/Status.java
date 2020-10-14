package com.apollo.backend.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Status extends GenericEntity {

    @NotNull(message = "Name is mandatory")
    @Size(min = 1, max = 300, message = "Name must be between 1 and 300 characters")
    private String name;

    protected Status() {
    }

    public Status(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}