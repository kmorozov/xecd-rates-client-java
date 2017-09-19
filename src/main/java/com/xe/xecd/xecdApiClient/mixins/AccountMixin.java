package com.xe.xecd.xecdApiClient.mixins;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountMixin {
  @JsonProperty("package")
  private String productPackage;

  @JsonProperty("service_start_timestamp")
  private Date serviceStartDate;
  
  @JsonProperty("package_limit_reset")
  private Date packageLimitResetDate;
}
