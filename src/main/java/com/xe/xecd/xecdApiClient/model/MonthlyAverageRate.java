package com.xe.xecd.xecdApiClient.model;

public class MonthlyAverageRate {
  private Double rate;
  private Double rateInverse;
  private Integer month;
  private Integer days;

  public Double getRateInverse() {
    return rateInverse;
  }

  public void setRateInverse(Double rateInverse) {
    this.rateInverse = rateInverse;
  }

  public MonthlyAverageRate() {
    //
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public Integer getMonth() {
    return month;
  }

  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public Integer getDays() {
    return days;
  }

  public void setDays(Integer days) {
    this.days = days;
  }
}
