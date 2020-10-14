package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryDomain {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private EntityManager entityManager;
}