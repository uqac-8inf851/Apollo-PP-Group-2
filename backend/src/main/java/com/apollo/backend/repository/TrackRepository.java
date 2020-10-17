package com.apollo.backend.repository;

import java.util.List;

import com.apollo.backend.model.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "track", path = "track")
public interface TrackRepository extends PagingAndSortingRepository<Track, Integer> {

  List<Track> findById(@Param("id") String id);

  @Override
  @RestResource(exported = false)
  <S extends Track> S save(S track);
}