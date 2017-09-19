package com.xe.xecd.xecdApiClient.mixins;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConvertMixin {
  @JsonProperty("timestamp")
  private Date date;
}
