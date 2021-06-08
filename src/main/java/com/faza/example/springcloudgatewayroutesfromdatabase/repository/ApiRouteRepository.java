package com.faza.example.springcloudgatewayroutesfromdatabase.repository;

import com.faza.example.springcloudgatewayroutesfromdatabase.model.database.ApiRoute;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ApiRouteRepository extends ReactiveCrudRepository<ApiRoute, Long> {
  
}
