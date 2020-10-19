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
public class Task extends GenericEntity {

    @NotNull(message = "Title is mandatory")
    @Size(min = 1, max = 300, message = "Title must be between 1 and 300 characters")
    private String title;

    @NotNull(message = "Description is mandatory")
    @Size(min = 1, max = 300, message = "Description must be between 1 and 300 characters")
    private String description;

    private Integer priority;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task")
    private List<Track> tracks;

    protected Task() {
    }

    public Task(String title, String description, Integer priority, Project project) {
        this.setTitle(title);
        this.setDescription(description);
        this.setPriority(priority);
        this.setProject(project);
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Track> getTrack() {
        return tracks;
    }

    public void setTrack(List<Track> tracks) {
        this.tracks = tracks;
    }
}