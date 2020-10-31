package com.apollo.backend.repository;

import java.util.List;

import com.apollo.backend.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "category", path = "category")
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

  List<Category> findById(@Param("id") String id);

  @Override
  @RestResource(exported = false)
  <S extends Category> S save(S category);
}