package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.StatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusDomain {

    @Autowired
    private StatusRepository repository;

    @Autowired
    private EntityManager entityManager;
}