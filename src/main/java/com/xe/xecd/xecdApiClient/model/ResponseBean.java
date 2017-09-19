package com.xe.xecd.xecdApiClient.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseBean<T> {

  public static final String RATE_LIMIT_TOTAL_HEADER = "X-RateLimit-Limit";
  public static final String RATE_LIMIT_REMAINING_HEADER = "X-RateLimit-Remaining";
  public static final String RATE_LIMIT_RESET_TIMESTAMP_HEADER = "X-RateLimit-Reset";

  private Map<String, String> headers = new HashMap<String, String>();
  private T data = null;

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public void addHeader(String name, String value) {
    this.headers.put(name, value);
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Integer getTotalRateLimit() {
    String value = headers.get(RATE_LIMIT_TOTAL_HEADER);
    if (value == null) {
      return null;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public Integer getRemainingRateLimit() {
    String value = headers.get(RATE_LIMIT_REMAINING_HEADER);
    if (value == null) {
      return null;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public Date getRateLimitResetDate() {
    String value = headers.get(RATE_LIMIT_RESET_TIMESTAMP_HEADER);
    if (value == null) {
      return null;
    }
    try {
      return new Date(Long.parseLong(value) * 1000);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
