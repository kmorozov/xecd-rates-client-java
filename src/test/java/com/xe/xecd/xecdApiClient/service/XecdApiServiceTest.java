package com.xe.xecd.xecdApiClient.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xe.xecd.xecdApiClient.exception.ApiErrorCode;
import com.xe.xecd.xecdApiClient.exception.ErrorResponse;
import com.xe.xecd.xecdApiClient.exception.XecdRatesException;
import com.xe.xecd.xecdApiClient.model.Account;
import com.xe.xecd.xecdApiClient.model.ConvertFrom;
import com.xe.xecd.xecdApiClient.model.ConvertTo;
import com.xe.xecd.xecdApiClient.model.Currencies;
import com.xe.xecd.xecdApiClient.model.HistoricRatePeriod;
import com.xe.xecd.xecdApiClient.model.MonthlyAverage;
import com.xe.xecd.xecdApiClient.model.ResponseBean;
import com.xe.xecd.xecdApiClient.utils.JsonUtils;

public class XecdApiServiceTest {
  @Mock private XecdHttpClient httpClient;

  private XecdRatesClient apiService;

  private JsonNode testData;

  private JsonUtils jsonUtils = new JsonUtils();

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void init() throws XecdRatesException, IOException {
    MockitoAnnotations.initMocks(this);
    apiService = new XecdRatesClient(null, null);

    InputStream in = this.getClass().getClassLoader().getResourceAsStream("sample-data.json");
    InputStreamReader reader = new InputStreamReader(in);
    ObjectMapper parser = jsonUtils.getMapper();
    testData = parser.readTree(reader);
  }

  private boolean isMockingNeeded() {
    return System.getenv("XECD_SERVER_PREFIX") == null
        || System.getenv("XECD_ACCOUNT_ID") == null
        || System.getenv("XECD_API_KEY") == null;
  }

  @SuppressWarnings("unchecked")
  private void mockResponseIfNeeded(String testDataId, Integer httpStatusCode)
      throws ClientProtocolException, URISyntaxException, IOException, XecdRatesException {
    if (isMockingNeeded()) {
      ErrorResponse errorResponse =
          jsonUtils.fromJson(testData.get(testDataId).toString(), ErrorResponse.class);
      Mockito.when(
              httpClient.getResponse(
                  Mockito.anyString(),
                  Mockito.any(Class.class)))
          .thenThrow(new XecdRatesException(errorResponse, httpStatusCode));
    }
    ;
  }

  private <T> void mockResponseIfNeeded(Class<T> type, String testDataId)
      throws ClientProtocolException, URISyntaxException, IOException, XecdRatesException {
    if (isMockingNeeded()) {

      ResponseBean<T> bean = new ResponseBean<T>();
      bean.setData(jsonUtils.fromJson(testData.get(testDataId).toString(), type));
      Mockito.when(
              httpClient.getResponse(
                  Mockito.anyString(), Mockito.eq(type)))
          .thenReturn(bean);
    }
  }

  @Test
  public void testValidAccountInfo() throws URISyntaxException, IOException, XecdRatesException {
    mockResponseIfNeeded(Account.class, "AccountInfo");

    ResponseBean<Account> response = apiService.getAccountInfo();
    Assert.assertNotNull(response);
  }

  @Test
  public void testValidCurrencies() throws URISyntaxException, IOException, XecdRatesException {
    mockResponseIfNeeded(Currencies.class, "Currencies");

    ResponseBean<Currencies> response = apiService.getCurrencies(true, "en", null);
    Assert.assertNotNull(response);
    response = apiService.getCurrencies(true, "en", null);
    Assert.assertNotNull(response);
  }

  @Test
  public void testValidConvertFrom() throws URISyntaxException, IOException, XecdRatesException {
    mockResponseIfNeeded(ConvertFrom.class, "ConvertFrom");

    ResponseBean<ConvertFrom> response = apiService.convertFrom("CAD", "EUR", 1.00, false, false);
    Assert.assertNotNull(response);
    response = apiService.convertFrom("CAD", "EUR", 1.00, false, false);
    Assert.assertNotNull(response);
  }

  @Test
  public void testValidConvertTo() throws URISyntaxException, IOException, XecdRatesException {
    mockResponseIfNeeded(ConvertTo.class, "ConvertTo");

    ResponseBean<ConvertTo> response = apiService.convertTo("CAD", "EUR", 1.00, false, false);
    Assert.assertNotNull(response);
    response = apiService.convertTo("CAD", "EUR", 1.00, false, false);
    Assert.assertNotNull(response);
  }

  @Test
  public void testValidHistoricRate() throws URISyntaxException, IOException, XecdRatesException {
    mockResponseIfNeeded(ConvertFrom.class, "HistoricRate");

    ResponseBean<ConvertFrom> response =
        apiService.historicRate("CAD", "EUR,JPY", "2011-03-05", null, 1.00, false, false);
    Assert.assertNotNull(response);
    response = apiService.historicRate("CAD", "EUR,JPY", "2011-03-05", null, 1.00, false, false);
    Assert.assertNotNull(response);
  }

  @Test
  public void testValidHistoricRatePeriod()
      throws URISyntaxException, IOException, XecdRatesException {
    mockResponseIfNeeded(HistoricRatePeriod.class, "HistoricRatePeriod");

    ResponseBean<HistoricRatePeriod> response =
        apiService.historicRatePeriod(
            "USD",
            "CAD,EUR",
            1.00,
            "2011-02-11T12:00",
            "2011-06-02T12:00",
            "daily",
            1,
            500,
            null,
            null);
    Assert.assertNotNull(response);
    response =
        apiService.historicRatePeriod(
            "USD",
            "CAD,EUR",
            1.00,
            "2011-02-11T12:00",
            "2011-06-02T12:00",
            null,
            null,
            null,
            null,
            null);
    Assert.assertNotNull(response);
  }

  @Test
  public void testMonthlyAverage() throws URISyntaxException, IOException, XecdRatesException {
    mockResponseIfNeeded(MonthlyAverage.class, "MonthlyAverage");

    ResponseBean<MonthlyAverage> response =
        apiService.monthlyAverage("USD", "CAD", 1.00, 2016, 6, null, null);
    Assert.assertNotNull(response);
    response = apiService.monthlyAverage("USD", "CAD", 1.00, 2016, 6, null, null);
    Assert.assertNotNull(response);
  }

  // ***********************************************************
  // The following tests will not throw the expected exceptions
  // unless the environment variables are specified.
  // This is because the client calls are mocked away if the
  // config is not set up with the environment variables
  // ***********************************************************

  @Test
  @Ignore("can no longer provide username/password so this test cannot fail if the client is valid.")
  public void testInvalidAccountInfo()
      throws XecdRatesException, ClientProtocolException, URISyntaxException, IOException {
    mockResponseIfNeeded("InvalidAccountInfo", 401);
    expectedException.expect(
        new XecdApiErrorMatcher(ApiErrorCode.AUTHORIZATION_INVALID_CREDENTIALS));
    expectedException.expectMessage("Bad credentials");
    apiService.getAccountInfo();
  }

  @Test
  public void testInvalidConvertFrom()
      throws XecdRatesException, ClientProtocolException, URISyntaxException, IOException {
    mockResponseIfNeeded("InvalidConvertFrom", 400);
    expectedException.expect(
        new XecdApiErrorMatcher(ApiErrorCode.GENERAL_NO_DATA_SELECTED_CURRENCY_AND_DATE));
    apiService.convertFrom("ZZZ", "YYY", 1.0, false, false);
  }

  @Test
  public void testInvalidConvertTo()
      throws XecdRatesException, ClientProtocolException, URISyntaxException, IOException {
    mockResponseIfNeeded("InvalidConvertTo", 400);
    expectedException.expect(
        new XecdApiErrorMatcher(ApiErrorCode.GENERAL_NO_DATA_SELECTED_CURRENCY_AND_DATE));
    apiService.convertTo("ZZZ", "YYY", 1.0, false, false);
  }

  @Test
  public void testInvalidHistoricRate()
      throws XecdRatesException, ClientProtocolException, URISyntaxException, IOException {
    mockResponseIfNeeded("InvalidHistoricRate", 400);
    expectedException.expect(new XecdApiErrorMatcher(ApiErrorCode.HISTORIC_RATES_NO_DATA_DATE));
    apiService.historicRate("SZL", "CAD", "1980-06-27", "20:06", 1.0, false, false);
  }

  @Test
  public void testInvalidHistoricRatePeriod()
      throws XecdRatesException, ClientProtocolException, URISyntaxException, IOException {
    mockResponseIfNeeded("InvalidHistoricRatePeriod", 400);
    expectedException.expect(
        new XecdApiErrorMatcher(ApiErrorCode.HISTORIC_RATES_NO_DATA_DATE_RANGE));
    apiService.historicRatePeriod(
        "CAD", "USD", 1.0, "1000-10-10", "2000-10-10", null, null, null, false, false);
  }

  @Test
  public void testInvalidMonthlyAverageInvalidYear()
      throws XecdRatesException, ClientProtocolException, URISyntaxException, IOException {
    mockResponseIfNeeded("InvalidMonthlyAverageInvalidYear", 400);
    expectedException.expect(new XecdApiErrorMatcher(ApiErrorCode.MONTHLY_AVERAGE_INVALID_YEAR));
    apiService.monthlyAverage("CAD", "USD", 1.0, 3000, 01, false, false);
  }

  @Test
  public void testInvalidMonthlyAverageInvalidMonth()
      throws XecdRatesException, ClientProtocolException, URISyntaxException, IOException {
    mockResponseIfNeeded("InvalidMonthlyAverageInvalidMonth", 400);
    expectedException.expect(new XecdApiErrorMatcher(ApiErrorCode.MONTHLY_AVERAGE_INVALID_MONTH));
    apiService.monthlyAverage("CAD", "USD", 1.0, 2000, 21, false, false);
  }
}
