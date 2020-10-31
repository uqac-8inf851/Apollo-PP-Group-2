package com.apollo.backend.repository;

import java.util.List;

import com.apollo.backend.model.Task;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "task", path = "task")
public interface TaskRepository extends PagingAndSortingRepository<Task, Integer> {

  List<Task> findById(@Param("id") String id);

  @Override
  @RestResource(exported = false)
  <S extends Task> S save(S task);
}