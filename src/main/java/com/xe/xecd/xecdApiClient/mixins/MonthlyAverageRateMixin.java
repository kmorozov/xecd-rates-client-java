package com.xe.xecd.xecdApiClient.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MonthlyAverageRateMixin {
  @JsonProperty("monthlyAverage")
  private Double rate;

  @JsonProperty("monthlyAverageInverse")
  private Double rateInverse;

  @JsonProperty("daysInMonth")
  private Integer days;
}
