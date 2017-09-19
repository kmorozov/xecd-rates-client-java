package com.xe.xecd.xecdApiClient.exception;

public enum ApiErrorCode {
  GENERAL_SERVER_ERROR(0),
  GENERAL_USER_ERROR(6),
  GENERAL_NO_DATA_SELECTED_CURRENCY_AND_DATE(7),
  
  AUTHORIZATION_INVALID_CREDENTIALS(1),
  AUTHORIZATION_FAILED_LOGIN_LIMIT(2),
  AUTHORIZATION_FREE_TRIAL_ENDED(9),
  AUTHORIZATION_NO_HISTORIC_RATE_ACCESS(5),

  LIMIT_MONTHLY(3),
  LIMIT_THROTTLE(4),
  LIMIT_PAGE_SIZE(13),

  HISTORIC_RATES_NO_DATA_DATE(8),
  HISTORIC_RATES_NO_DATA_DATE_RANGE(10),
  HISTORIC_RATES_FUTURE_DATE(11),
  HISTORIC_RATES_REVERSED_DATE_RANGE(12),

  MONTHLY_AVERAGE_INVALID_YEAR(14),
  MONTHLY_AVERAGE_INVALID_MONTH(15);

  private int errorCode;

  private ApiErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public static ApiErrorCode fromErrorValue(int code) {
    for (ApiErrorCode enumInstance : values()) {
      if (enumInstance.errorCode == code) {
        return enumInstance;
      }
    }
    return null;
  }

  public int getErrorValue() {
    return errorCode;
  }
}
