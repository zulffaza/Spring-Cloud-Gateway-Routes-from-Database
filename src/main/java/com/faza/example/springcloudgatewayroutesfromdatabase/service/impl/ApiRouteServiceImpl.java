package com.faza.example.springcloudgatewayroutesfromdatabase.service.impl;

import com.faza.example.springcloudgatewayroutesfromdatabase.model.database.ApiRoute;
import com.faza.example.springcloudgatewayroutesfromdatabase.model.web.CreateOrUpdateApiRouteRequest;
import com.faza.example.springcloudgatewayroutesfromdatabase.repository.ApiRouteRepository;
import com.faza.example.springcloudgatewayroutesfromdatabase.service.ApiRouteService;
import com.faza.example.springcloudgatewayroutesfromdatabase.service.GatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ApiRouteServiceImpl implements ApiRouteService {
  
  @Autowired
  private ApiRouteRepository apiRouteRepository;
  
  @Autowired
  private GatewayRouteService gatewayRouteService;

  @Override
  public Flux<ApiRoute> findApiRoutes() {
    return apiRouteRepository.findAll();
  }

  @Override
  public Mono<ApiRoute> findApiRoute(Long id) {
    return findAndValidateApiRoute(id);
  }

  @Override
  public Mono<Void> createApiRoute(CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
    ApiRoute apiRoute = setNewApiRoute(new ApiRoute(), createOrUpdateApiRouteRequest);
    return apiRouteRepository.save(apiRoute)
        .doOnSuccess(obj -> gatewayRouteService.refreshRoutes())
        .then();
  }

  @Override
  public Mono<Void> updateApiRoute(Long id,
      CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
    return findAndValidateApiRoute(id)
        .map(apiRoute -> setNewApiRoute(apiRoute, createOrUpdateApiRouteRequest))
        .flatMap(apiRouteRepository::save)
        .doOnSuccess(obj -> gatewayRouteService.refreshRoutes())
        .then();
  }

  @Override
  public Mono<Void> deleteApiRoute(Long id) {
    return findAndValidateApiRoute(id)
        .flatMap(apiRoute -> apiRouteRepository.deleteById(apiRoute.getId()))
        .doOnSuccess(obj -> gatewayRouteService.refreshRoutes());
  }
  
  private Mono<ApiRoute> findAndValidateApiRoute(Long id) {
    return apiRouteRepository.findById(id)
        .switchIfEmpty(Mono.error(
            new RuntimeException(String.format("Api route with id %d not found", id))));
  }
  
  private ApiRoute setNewApiRoute(ApiRoute apiRoute,
      CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
    apiRoute.setPath(createOrUpdateApiRouteRequest.getPath());
    apiRoute.setMethod(createOrUpdateApiRouteRequest.getMethod());
    apiRoute.setUri(createOrUpdateApiRouteRequest.getUri());
    return apiRoute;
  }
}
