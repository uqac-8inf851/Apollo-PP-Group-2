package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectDomain {

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private EntityManager entityManager;
}