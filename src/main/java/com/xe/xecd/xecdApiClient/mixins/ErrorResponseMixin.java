package com.xe.xecd.xecdApiClient.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xe.xecd.xecdApiClient.exception.ApiErrorCode;

public class ErrorResponseMixin {
  @JsonIgnore private ApiErrorCode errorCode;

  @JsonProperty("code")
  private int errorValue;
}
