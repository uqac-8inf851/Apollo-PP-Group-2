package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDomain {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EntityManager entityManager;
}