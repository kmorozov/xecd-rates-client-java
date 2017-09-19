package com.xe.xecd.xecdApiClient.model;

public class Currency {
  private String iso;
  private String name;
  private Boolean obsolete;
  private Currency successor;
  private String currencySymbol;
  private String currencySymbolOnRight;

  public Currency() {}

  public Currency(String isoCurrencyCode) {
    this.iso = isoCurrencyCode;
  }

  public String getIso() {
    return iso;
  }

  public void setIso(String iso) {
    this.iso = iso;
  }

  public String getCurrencyName() {
    return name;
  }

  public void setCurrencyName(String name) {
    this.name = name;
  }

  public Boolean getObsolete() {
    return obsolete;
  }

  public void setObsolete(Boolean obsolete) {
    this.obsolete = obsolete;
  }

  public Currency getSuccessor() {
    return successor;
  }

  public void setSuccessor(Currency successor) {
    this.successor = successor;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getCurrencySymbolOnRight() {
    return currencySymbolOnRight;
  }

  public void setCurrencySymbolOnRight(String currencySymbolOnRight) {
    this.currencySymbolOnRight = currencySymbolOnRight;
  }
}
