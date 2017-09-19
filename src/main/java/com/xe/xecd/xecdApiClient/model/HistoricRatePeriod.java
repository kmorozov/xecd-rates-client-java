package com.xe.xecd.xecdApiClient.model;

import java.util.List;
import java.util.Map;

public class HistoricRatePeriod extends LegalEntity {
  private String from;
  private Double amount;
  private Map<String, List<Conversion>> to;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Map<String, List<Conversion>> getTo() {
    return to;
  }

  public void setTo(Map<String, List<Conversion>> to) {
    this.to = to;
  }
}
