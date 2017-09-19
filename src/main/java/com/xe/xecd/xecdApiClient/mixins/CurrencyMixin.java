package com.xe.xecd.xecdApiClient.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyMixin {
  @JsonProperty("currency_name")
  private String name;

  @JsonProperty("is_obsolete")
  private Boolean obsolete;
  
  @JsonProperty("superceded_by")
  private String successor;
}
