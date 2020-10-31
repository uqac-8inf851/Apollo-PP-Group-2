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
    @JoinTable(name = "team_person", 
      joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"), 
      inverseJoinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"))
    private List<Person> persons;

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

    public List<Person> getPerson() {
        return persons;
    }

    public void setPerson(List<Person> persons) {
        this.persons = persons;
    }
}