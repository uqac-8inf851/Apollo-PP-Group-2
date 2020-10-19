package com.apollo.backend.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Team extends GenericEntity {

    @NotNull(message = "Name is mandatory")
    @Size(min = 1, max = 300, message = "Name must be between 1 and 300 characters")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "team_user", 
      joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"), 
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;

    protected Team() {
    }

    public Team(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUser() {
        return users;
    }

    public void setUser(List<User> users) {
        this.users = users;
    }
}