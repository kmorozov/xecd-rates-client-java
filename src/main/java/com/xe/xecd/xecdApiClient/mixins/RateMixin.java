package com.xe.xecd.xecdApiClient.mixins;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xe.xecd.xecdApiClient.model.Currency;
import com.xe.xecd.xecdApiClient.model.LegalEntity;

public class RateMixin extends LegalEntity {
  @JsonProperty("quotecurrency")
  private Currency currency;

  @JsonProperty("mid")
  private Double rate;

  @JsonProperty("timestamp")
  private Date date;
}
