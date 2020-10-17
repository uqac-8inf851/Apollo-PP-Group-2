package com.apollo.backend.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Track extends GenericEntity {

    @NotNull(message = "Start Time is mandatory")
    private Instant startTime;

    private Instant endTime;

    protected Track() {
    }

    public Track(Instant startTime, Instant endTime) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
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
}