package com.apollo.backend.model;

import javax.persistence.Entity;
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

    protected Task() {
    }

    public Task(String title, String description, Integer priority) {
        this.setTitle(title);
        this.setDescription(description);
        this.setPriority(priority);
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
}