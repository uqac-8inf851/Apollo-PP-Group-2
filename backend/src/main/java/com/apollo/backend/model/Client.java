package com.apollo.backend.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Client extends GenericEntity {

    @NotNull(message = "Company Name is mandatory")
    @Size(min = 1, max = 300, message = "Company Name must be between 1 and 300 characters")
    private String companyName;

    protected Client() {
    }

    public Client(String companyName) {
        this.setCompanyName(companyName);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}