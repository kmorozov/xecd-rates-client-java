package com.xe.xecd.xecdApiClient.service;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.xe.xecd.xecdApiClient.exception.ApiErrorCode;
import com.xe.xecd.xecdApiClient.exception.XecdRatesException;

public class XecdApiErrorMatcher extends BaseMatcher<XecdRatesException> {

  private ApiErrorCode expectedErrorCode;

  public XecdApiErrorMatcher(ApiErrorCode expectedErrorCode) {
    this.expectedErrorCode = expectedErrorCode;
  }

  @Override
  public boolean matches(Object item) {
    if (item instanceof XecdRatesException) {
      return ((XecdRatesException) item).getErrorResponse().getErrorCode().equals(expectedErrorCode);
    }

    return false;
  }

  @Override
  public void describeTo(Description description) {
	  description.appendText("Exception with error code " + expectedErrorCode.name());
  }
}
