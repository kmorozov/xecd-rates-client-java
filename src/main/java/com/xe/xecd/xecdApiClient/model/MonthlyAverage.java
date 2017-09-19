package com.xe.xecd.xecdApiClient.model;

import java.util.List;
import java.util.Map;

public class MonthlyAverage extends LegalEntity {
  private String from;
  private Double amount;
  private Integer year;
  private Map<Currency, List<MonthlyAverageRate>> to;

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

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Map<Currency, List<MonthlyAverageRate>> getTo() {
    return to;
  }

  public void setTo(Map<Currency, List<MonthlyAverageRate>> to) {
    this.to = to;
  }
}
