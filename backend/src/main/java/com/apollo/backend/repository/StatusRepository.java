package com.apollo.backend.repository;

import java.util.List;

import com.apollo.backend.model.Status;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "status", path = "status")
public interface StatusRepository extends PagingAndSortingRepository<Status, Integer> {

  List<Status> findById(@Param("id") String id);

  @Override
  @RestResource(exported = false)
  <S extends Status> S save(S status);
}