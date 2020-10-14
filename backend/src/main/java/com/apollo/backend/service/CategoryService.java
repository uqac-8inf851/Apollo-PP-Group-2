package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.CategoryDomain;
import com.apollo.backend.model.Category;
import com.apollo.backend.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("categoryService")
public class CategoryService implements CategoryRepository {

    @Autowired
    @Qualifier("categoryRepository")
    private CategoryRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Category> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<Category> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Category> findById(String id) {
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
    public void delete(Category entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends Category> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends Category> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends Category> S save(S category) {

        CategoryDomain categoryDomain = applicationContext.getBean(CategoryDomain.class);
        
        return repository.save(category);
    }
}
