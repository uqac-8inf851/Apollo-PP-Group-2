package com.apollo.backend.repository;

import java.util.List;

import com.apollo.backend.model.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

  List<User> findById(@Param("id") String id);

  @Override
  @RestResource(exported = false)
  <S extends User> S save(S user);
}