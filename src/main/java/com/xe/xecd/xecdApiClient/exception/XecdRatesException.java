package com.xe.xecd.xecdApiClient.exception;

/** Thrown if the XECD rates api returns an error, or if there is an error in configuration */
public class XecdRatesException extends Exception {

  private Integer httpStatusCode;

  private ErrorResponse errorResponse;

  private static final long serialVersionUID = -4024493361826460130L;

  public XecdRatesException(Throwable e) {
    super(e);
  }

  public XecdRatesException(String message) {
    super(message);
  }

  public XecdRatesException(ErrorResponse errorResponse, Integer httpStatusCode) {
    super(generateMessage(errorResponse));
    this.errorResponse = errorResponse;
    this.httpStatusCode = httpStatusCode;
  }

  public XecdRatesException(String message, Throwable cause) {
    super(message, cause);
  }

  public XecdRatesException(Throwable cause, ErrorResponse errorResponse, Integer httpStatusCode) {
    super(generateMessage(errorResponse), cause);
    this.errorResponse = errorResponse;
    this.httpStatusCode = httpStatusCode;
  }

  public ErrorResponse getErrorResponse() {
    return errorResponse;
  }

  public void setErrorResponse(ErrorResponse errorResponse) {
    this.errorResponse = errorResponse;
  }

  public Integer getHttpStatusCode() {
    return httpStatusCode;
  }

  public void setHttpStatusCode(Integer httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  private static String generateMessage(ErrorResponse error) {
    return String.format("[%s] %s", error.getErrorCode().name(), error.getMessage());
  }
}
