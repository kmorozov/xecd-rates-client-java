package com.xe.xecd.xecdApiClient.model;

import java.util.Date;
import java.util.List;

public class ConvertTo extends LegalEntity {
  private String to;
  private Double amount;
  private Date date;
  private List<Conversion> from;

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
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

  public List<Conversion> getFrom() {
    return from;
  }

  public void setFrom(List<Conversion> from) {
    this.from = from;
  }
}
