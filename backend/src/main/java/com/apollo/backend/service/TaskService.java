package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.TaskDomain;
import com.apollo.backend.model.Task;
import com.apollo.backend.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("taskService")
public class TaskService implements TaskRepository {

    @Autowired
    @Qualifier("taskRepository")
    private TaskRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Task> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<Task> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Task> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Task entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends Task> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends Task> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends Task> S save(S task) {

        TaskDomain taskDomain = applicationContext.getBean(TaskDomain.class);
        
        return repository.save(task);
    }
}
