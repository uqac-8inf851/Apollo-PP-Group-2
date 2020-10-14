package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskDomain {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private EntityManager entityManager;
}