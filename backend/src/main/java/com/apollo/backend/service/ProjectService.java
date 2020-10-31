package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.ProjectDomain;
import com.apollo.backend.model.Project;
import com.apollo.backend.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("projectService")
public class ProjectService implements ProjectRepository {

    @Autowired
    @Qualifier("projectRepository")
    private ProjectRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<Project> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Project> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Project> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<Project> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<Project> findById(Integer idProject) {
        return repository.findById(idProject);
    }

    @Override
    public List<Project> findById(String idProject) {
        return repository.findById(idProject);
    }

    @Override
    public boolean existsById(Integer idProject) {
        return repository.existsById(idProject);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Integer idProject) {
        repository.deleteById(idProject);
    }

    @Override
    public void delete(Project entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends Project> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends Project> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends Project> S save(S project) {

        ProjectDomain projectDomain = applicationContext.getBean(ProjectDomain.class);
        
        return repository.save(project);
    }
}
