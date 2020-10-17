package com.apollo.backend.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Program extends GenericEntity {

    @NotNull(message = "Title is mandatory")
    @Size(min = 1, max = 300, message = "Title must be between 1 and 300 characters")
    private String title;

    @NotNull(message = "Description is mandatory")
    @Size(min = 1, max = 300, message = "Description must be between 1 and 300 characters")
    private String description;

    @OneToMany(mappedBy = "program")
    private List<Project> projects;

    protected Program() {
    }

    public Program(String title, String description) {
        this.setTitle(title);
        this.setDescription(description);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}