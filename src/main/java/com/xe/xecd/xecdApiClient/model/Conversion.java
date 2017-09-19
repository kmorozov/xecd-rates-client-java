package com.xe.xecd.xecdApiClient.model;

import java.util.Date;

public class Conversion extends LegalEntity {
  private Currency currency;
  private Double rate;
  private Date date;

  public Conversion() {
    //default
  }

  public Conversion(Currency quotecurrency, Double rate) {
    this.currency = quotecurrency;
    this.rate = rate;
  }

  public Currency getQuotecurrency() {
    return currency;
  }

  public void setQuotecurrency(Currency quotecurrency) {
    this.currency = quotecurrency;
  }

  public Double getMid() {
    return rate;
  }

  public void setMid(Double mid) {
    this.rate = mid;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
