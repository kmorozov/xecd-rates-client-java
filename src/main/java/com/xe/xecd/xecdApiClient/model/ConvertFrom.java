package com.xe.xecd.xecdApiClient.model;

import java.util.Date;
import java.util.List;

public class ConvertFrom extends LegalEntity {
  private String from;
  private Double amount;
  private Date date;
  private List<Conversion> to;

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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public List<Conversion> getTo() {
    return to;
  }

  public void setTo(List<Conversion> to) {
    this.to = to;
  }
}
