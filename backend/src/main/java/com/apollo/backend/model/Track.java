package com.apollo.backend.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Track extends GenericEntity {

    @NotNull(message = "Start Time is mandatory")
    private Instant startTime;

    private Instant endTime;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    protected Track() {
    }

    public Track(Instant startTime, Instant endTime, Task task) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setTask(task);
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}