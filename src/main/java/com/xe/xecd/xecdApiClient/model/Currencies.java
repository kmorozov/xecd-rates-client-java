package com.xe.xecd.xecdApiClient.model;

import java.util.ArrayList;
import java.util.List;

public class Currencies extends LegalEntity {
  private List<Currency> currencies = new ArrayList<Currency>();

  public static Currencies asList(String... currencyCodes) {
    Currencies newCurrencies = new Currencies();
    for (String currencyCode : currencyCodes) {
      newCurrencies.currencies.add(new Currency(currencyCode));
    }
    return newCurrencies;
  }

  public List<Currency> getCurrencies() {
    return currencies;
  }

  public void setCurrencies(List<Currency> currencies) {
    this.currencies = currencies;
  }
}
