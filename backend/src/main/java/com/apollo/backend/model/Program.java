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
    private String programTitle;

    @NotNull(message = "Description is mandatory")
    @Size(min = 1, max = 300, message = "Description must be between 1 and 300 characters")
    private String programDescription;

    @OneToMany(mappedBy = "program")
    private List<Project> projects;

    protected Program() {
    }

    public Program(String programTitle, String programDescription) {
        this.setProgramTitle(programTitle);
        this.setProgramDescription(programDescription);
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public String getProgramDescription() {
        return programDescription;
    }

    public void setProgramDescription(String programDescription) {
        this.programDescription = programDescription;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}