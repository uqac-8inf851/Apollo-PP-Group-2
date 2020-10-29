package com.apollo.backend.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Project extends GenericEntity {

    @NotNull(message = "Title is mandatory")
    @Size(min = 1, max = 300, message = "Title must be between 1 and 300 characters")
    private String projectTitle;

    @NotNull(message = "Description is mandatory")
    @Size(min = 1, max = 300, message = "Description must be between 1 and 300 characters")
    private String projectDescription;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    private Program program;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    protected Project() {
    }

    public Project(String projectTitle, String projectDescription, Program program) {
        this.setProjectTitle(projectTitle);
        this.setProjectDescription(projectDescription);
        this.setProgram(program);
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}