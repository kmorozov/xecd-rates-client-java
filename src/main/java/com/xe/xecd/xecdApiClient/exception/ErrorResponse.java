package com.xe.xecd.xecdApiClient.exception;

public class ErrorResponse {
  private ApiErrorCode errorCode;
  private int errorValue;
  private String message;
  private String documentation_url;

  public ErrorResponse() {}

  public ErrorResponse(int code, String message, String documentation_url) {
    this.errorValue = code;
    this.message = message;
    this.documentation_url = documentation_url;
    this.errorCode = ApiErrorCode.fromErrorValue(code);
  }

  public int getErrorValue() {
    return errorValue;
  }

  public void setErrorValue(int value) {
    this.errorValue = value;
    this.errorCode = ApiErrorCode.fromErrorValue(value);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDocumentation_url() {
    return documentation_url;
  }

  public void setDocumentation_url(String documentation_url) {
    this.documentation_url = documentation_url;
  }

  public ApiErrorCode getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(ApiErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
