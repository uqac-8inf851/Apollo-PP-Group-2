package com.apollo.backend.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public class GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Add By is mandatory")
    private String addBy;

    @CreationTimestamp
    @Column(name = "addDate", nullable = false, updatable = false, insertable = true)
    private Instant addDate;

    private String modBy;

    @UpdateTimestamp
    @Column(name = "modDate", nullable = true, updatable = true, insertable = false)
    private Instant modDate;

    protected GenericEntity() {
        this.setAddBy("TODO");
        this.setModBy("TODO");
    }

    public Integer getId() {
        return id;
    }    

    public String getModBy() {
        return modBy;
    }

    private void setModBy(String modBy) {
        this.modBy = modBy;
    }    

    public Instant getModDate() {
        return modDate;
    }

    public String getAddBy() {
        return addBy;
    }

    private void setAddBy(String addBy) {
        this.addBy = addBy;
    }
    
    public Instant getAddDate() {
        return addDate;
    }  
}