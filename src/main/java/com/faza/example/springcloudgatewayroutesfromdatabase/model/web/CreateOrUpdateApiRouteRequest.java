package com.faza.example.springcloudgatewayroutesfromdatabase.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrUpdateApiRouteRequest {

  @NotBlank
  private String path;

  private String method;

  @NotBlank
  private String uri;
}
