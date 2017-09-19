package com.xe.xecd.xecdApiClient.service;

import java.io.IOException;
import java.text.MessageFormat;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xe.xecd.xecdApiClient.config.XecdApiConfigBean;
import com.xe.xecd.xecdApiClient.exception.XecdRatesException;
import com.xe.xecd.xecdApiClient.model.Account;
import com.xe.xecd.xecdApiClient.model.ConvertFrom;
import com.xe.xecd.xecdApiClient.model.ConvertTo;
import com.xe.xecd.xecdApiClient.model.Currencies;
import com.xe.xecd.xecdApiClient.model.HistoricRatePeriod;
import com.xe.xecd.xecdApiClient.model.MonthlyAverage;
import com.xe.xecd.xecdApiClient.model.ResponseBean;

public class XecdRatesClient {

  private static Logger logger = LoggerFactory.getLogger(XecdRatesClient.class);

  private static final ParameterDefinition accountIdParameter =
      new ParameterDefinition()
          .required()
          .withOptionName("getAccountId()")
          .withEnvironmentVariableName("XECD_ACCOUNT_ID");

  private static final ParameterDefinition apiKeyParameter =
      new ParameterDefinition()
          .required()
          .secret()
          .withOptionName("getApiKey()")
          .withEnvironmentVariableName("XECD_API_KEY");

  private static final ParameterDefinition serverPrefixParameter =
      new ParameterDefinition()
          .withOptionName("getServerPrefix()")
          .withEnvironmentVariableName("XECD_SERVER_PREFIX")
          .withDefaultValue("https://xecdapi.xe.com");

  private static final String accountInfoUrl = "{0}/v1/account_info";
  private static final String currenciesUrl = "{0}/v1/currencies/?";
  private static final String convertFromUrl = "{0}/v1/convert_from?to={1}";
  private static final String convertToUrl = "{0}/v1/convert_to?from={1}";
  private static final String historicRateUrl = "{0}/v1/historic_rate/?to={1}&date={2}";
  private static final String historicRatePeriodUrl = "{0}/v1/historic_rate/period/?to={1}";
  private static final String monthlyAverageUrl = "{0}/v1/monthly_average?to={1}";

  private final XecdApiConfigBean config;

  private final XecdHttpClient wsClient;

  /**
   * Construct the XECD Rates client. The rates client will take its configuration from the provided
   * configuration bean unless specific configurations are overridden by environment variables.
   *
   * <p>If not provided, the server prefix defaults to "https://xecdapi.xe.com".
   *
   * <p>The used environment variables are: XECD_ACCOUNT_ID XECD_API_KEY XECD_BASE_URL
   *
   * @param configParameter A configuration bean for the rates client, or NULL.
   * @param client
   * @throws XecdRatesException
   */
  public XecdRatesClient(XecdApiConfigBean configParameter, HttpClient client)
      throws XecdRatesException {

    if (configParameter == null) {
      this.config = new XecdApiConfigBean();
    } else {
      this.config = configParameter;
    }

    config.setAccountId(resolveVariable(config.getAccountId(), accountIdParameter));
    config.setApiKey(resolveVariable(config.getApiKey(), apiKeyParameter));
    config.setServerPrefix(resolveVariable(config.getServerPrefix(), serverPrefixParameter));

    if (client == null) {
      client = buildDefaultHttpClient(config);
    }
    this.wsClient = new XecdHttpClient(client, config.getAccountId(), config.getApiKey());
  }

  private static String resolveVariable(String defaultValue, ParameterDefinition parameterDefnition)
      throws XecdRatesException {

    String envVariable = System.getenv(parameterDefnition.getEnvironmentVariableName());
    if (envVariable != null && !envVariable.isEmpty()) {
      if (parameterDefnition.isSecret()) {
        logger.debug(
            "Read {} from the environment: {}",
            parameterDefnition.getEnvironmentVariableName(),
            envVariable);
      } else {
        logger.debug(
            "Read {} from the environment", parameterDefnition.getEnvironmentVariableName());
      }
      return envVariable;
    } else if (defaultValue != null) {
      if (parameterDefnition.isSecret()) {
        logger.debug("Read {} from the configuration: {}", defaultValue, envVariable);
      } else {
        logger.debug("Read {} from the configuration", defaultValue);
      }
    }
    if (parameterDefnition.getDefaultValue() != null) {
      return parameterDefnition.getDefaultValue();
    }

    if (parameterDefnition.isRequired()) {
      throw new XecdRatesException(
          String.format(
              "missing %s, and this variable is required.", parameterDefnition.getOptionName()));
    }
    return null;
  }

  private static HttpClient buildDefaultHttpClient(XecdApiConfigBean config)
      throws XecdRatesException {
    HttpClientBuilder builder = HttpClientBuilder.create();
    if (config.getConnectTimeout() != null) {
      RequestConfig requestConfig =
          RequestConfig.custom().setConnectTimeout(config.getConnectTimeout()).build();
      builder.setDefaultRequestConfig(requestConfig);
    }
    try {
      SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
      sslContext.init(null, null, null);
      builder.setSSLContext(sslContext);
      return builder.build();
    } catch (Exception e) {
      throw new XecdRatesException("Could not establish SSL Context properly for HTTPClient.", e);
    }
  }

  /**
   * Returns basic information about your XECD account
   *
   * @return AccountInfoResponse object containing account information for the XECD API account used
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<Account> getAccountInfo() throws XecdRatesException, IOException {
    ResponseBean<Account> response = null;
    String accountInfoUrlStr = MessageFormat.format(accountInfoUrl, config.getServerPrefix());
    logger.debug("Calling {}", accountInfoUrlStr);
    response = wsClient.getResponse(accountInfoUrlStr, Account.class);
    return response;
  }

  /**
   * Fetches a list of requested currencies, active and obsolete
   *
   * @param obsolete Specifies whether to return obsolete currencies
   * @param language The language to return the currency codes in. Languages include: "ar", "de",
   *     "en", "es", "fr", "it", "ja", "pt", "sv", "zh-CN", and "zh-HK"
   * @param iso Comma-separated list of ISO 4217 codes that specifies currencies provided
   * @return CurrenciesResponse containing the list of all currencies
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<Currencies> getCurrencies(Boolean obsolete, String language, String iso)
      throws XecdRatesException, IOException {
    ResponseBean<Currencies> response = null;

    String currenciesString = MessageFormat.format(currenciesUrl, config.getServerPrefix());
    currenciesString += (obsolete != null) ? "obsolete=" + obsolete.toString() : "";
    currenciesString += (language != null && !language.isEmpty()) ? "&language=" + language : "";
    currenciesString += (iso != null && !iso.isEmpty()) ? "&iso=" + iso : "";

    logger.debug("Calling {}", currenciesString);
    response = wsClient.getResponse(currenciesString, Currencies.class);

    return response;
  }

  /**
   * Fetches a list of all currencies, active and obsolete
   *
   * @param obsolete Specifies whether to return obsolete currencies
   * @param language The language to return the currency codes in. Languages include: "ar", "de",
   *     "en", "es", "fr", "it", "ja", "pt", "sv", "zh-CN", and "zh-HK"
   * @return CurrenciesResponse containing the list of all currencies
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<Currencies> getCurrencies(Boolean obsolete, String language)
      throws XecdRatesException, IOException {
    return getCurrencies(obsolete, language, null);
  }

  /**
   * Converts from a currency amount to multiple other currencies
   *
   * @param from The currency you want to convert from
   * @param to Comma separated list of to currencies. Use * to convert to all currencies.
   * @param amount The amount you want to convert
   * @param obsolete Specifies whether to return obsolete currencies
   * @param inverse Specifies whether to return inverse rates
   * @return ConvertFromResponse object containing the converted currencies
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<ConvertFrom> convertFrom(
      String from, String to, Double amount, Boolean obsolete, Boolean inverse)
      throws XecdRatesException, IOException {
    ResponseBean<ConvertFrom> response = null;

    String convertFromString = MessageFormat.format(convertFromUrl, config.getServerPrefix(), to);

    convertFromString += (from != null && !from.isEmpty()) ? "&from=" + from : "";
    convertFromString += (amount != null && amount != 0) ? "&amount=" + amount : "";
    convertFromString += (obsolete != null) ? "&obsolete=" + obsolete.toString() : "";
    convertFromString += (inverse != null) ? "&inverse=" + inverse.toString() : "";

    logger.debug("Calling {}", convertFromString);

    response = wsClient.getResponse(convertFromString, ConvertFrom.class);
    return response;
  }

  /**
   * Converts from a currency amount to multiple other currencies
   *
   * @param from The currency you want to convert from
   * @param to Comma separated list of to currencies. Use * to convert to all currencies.
   * @param amount The amount you want to convert
   * @param obsolete Specifies whether to return obsolete currencies
   * @return ConvertFromResponse object containing the converted currencies
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<ConvertFrom> convertFrom(
      String from, String to, Double amount, Boolean obsolete)
      throws XecdRatesException, IOException {
    return convertFrom(from, to, amount, obsolete, null);
  }

  /**
   * Converts to a currency amount from multiple other currencies
   *
   * @param to The currency you want to convert to
   * @param from Comma separated list of from currencies. Use * to convert all currencies.
   * @param amount The amount you want to convert
   * @param obsolete Specifies whether to return obsolete currencies
   * @param inverse Specifies whether to return inverse rates
   * @return ConvertToResponse object containing the converted currencies
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<ConvertTo> convertTo(
      String to, String from, Double amount, Boolean obsolete, Boolean inverse)
      throws XecdRatesException, IOException {
    ResponseBean<ConvertTo> response = null;

    String convertToString = MessageFormat.format(convertToUrl, config.getServerPrefix(), from);

    convertToString += (to != null && !to.isEmpty()) ? "&to=" + to : "";
    convertToString += (amount != null && amount != 0) ? "&amount=" + amount : "";
    convertToString += (obsolete != null) ? "&obsolete=" + obsolete.toString() : "";
    convertToString += (inverse != null) ? "&inverse=" + inverse.toString() : "";

    logger.debug("Calling {}", convertToString);

    response = wsClient.getResponse(convertToString, ConvertTo.class);
    return response;
  }

  /**
   * Converts to a currency amount from multiple other currencies
   *
   * @param to The currency you want to convert to
   * @param from Comma separated list of from currencies. Use * to convert all currencies.
   * @param amount The amount you want to convert
   * @param obsolete Specifies whether to return obsolete currencies
   * @return ConvertToResponse object containing the converted currencies
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<ConvertTo> convertTo(String to, String from, Double amount, Boolean obsolete)
      throws XecdRatesException, IOException {
    return convertTo(to, from, amount, obsolete, null);
  }

  /**
   * Returns the historic rate for a single base currency and one or more counter currencies
   *
   * @param from The currency you want to convert from
   * @param to Comma separated list of to currencies. Use * to convert to all currencies.
   * @param date UTC date representing the historic date you want to convert to in format of
   *     YYYY-MM-DD
   * @param time Specific time of day in UTC format of HH:MM
   * @param amount The amount you want to convert
   * @param obsolete Specifies whether to return obsolete currencies
   * @param inverse Specifies whether to return inverse rates
   * @return HistoricRateResponse object containing the historic rate data
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<ConvertFrom> historicRate(
      String from,
      String to,
      String date,
      String time,
      Double amount,
      Boolean obsolete,
      Boolean inverse)
      throws XecdRatesException, IOException {
    ResponseBean<ConvertFrom> response = null;

    String historicRateString =
        MessageFormat.format(historicRateUrl, config.getServerPrefix(), to, date);

    historicRateString += (from != null && !from.isEmpty()) ? "&from=" + from : "";
    historicRateString += (time != null && !time.isEmpty()) ? "&time=" + time : "";
    historicRateString += (amount != null && amount != 0) ? "&amount=" + amount : "";
    historicRateString += (obsolete != null) ? "&obsolete=" + obsolete.toString() : "";
    historicRateString += (inverse != null) ? "&inverse=" + inverse.toString() : "";

    logger.debug("Calling {}", historicRateString);
    response = wsClient.getResponse(historicRateString, ConvertFrom.class);
    return response;
  }

  /**
   * Returns the historic rate for a single base currency and one or more counter currencies
   *
   * @param from The currency you want to convert from
   * @param to Comma separated list of to currencies. Use * to convert to all currencies.
   * @param date UTC date representing the historic date you want to convert to in format of
   *     YYYY-MM-DD
   * @param time Specific time of day in UTC format of HH:MM
   * @param amount The amount you want to convert
   * @param obsolete Specifies whether to return obsolete currencies
   * @return HistoricRateResponse object containing the historic rate data
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<ConvertFrom> historicRate(
      String from, String to, String date, String time, Double amount, Boolean obsolete)
      throws XecdRatesException, IOException {
    return historicRate(from, to, date, time, amount, obsolete, null);
  }

  /**
   * Returns a daily historic rate for a single base currency and one or more counter currencies
   * over a period of time
   *
   * @param from The currency you want to convert from
   * @param to Comma separated list of to currencies. Use * to convert to all currencies.
   * @param amount The amount you want to convert
   * @param start The starting timestamp in the UTC format yyyy-mm-ddThh:mm
   * @param end The ending timestamp in the UTC format yyyy-mm-ddThh:mm
   * @param interval Specifies daily or hourly data, interval is daily by default
   * @param page The page number to request. The default is the first page (page 1)
   * @param perPage The number of results per page. Default is 30 per page up to a maximum of 100
   *     per page
   * @param obsolete Specifies whether to return obsolete currencies
   * @param inverse Specifies whether to return inverse rates
   * @return HistoricRatePeriodResponse object containing the daily historical rates data
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<HistoricRatePeriod> historicRatePeriod(
      String from,
      String to,
      Double amount,
      String start,
      String end,
      String interval,
      Integer page,
      Integer perPage,
      Boolean obsolete,
      Boolean inverse)
      throws XecdRatesException, IOException {
    ResponseBean<HistoricRatePeriod> response = null;

    String historicRatePeriodString =
        MessageFormat.format(historicRatePeriodUrl, config.getServerPrefix(), to);

    historicRatePeriodString += (from != null && !from.isEmpty()) ? "&from=" + from : "";
    historicRatePeriodString += (amount != null && amount != 0) ? "&amount=" + amount : "";
    historicRatePeriodString +=
        (start != null && !end.isEmpty()) ? "&start_timestamp=" + start : "";
    historicRatePeriodString += (end != null && !end.isEmpty()) ? "&end_timestamp=" + end : "";
    historicRatePeriodString +=
        (interval != null && !interval.isEmpty()) ? "&interval=" + interval : "";
    historicRatePeriodString += (page != null && page != 0) ? "&page=" + page : "";
    historicRatePeriodString += (perPage != null && perPage != 0) ? "&per_page=" + perPage : "";
    historicRatePeriodString += (obsolete != null) ? "&obsolete=" + obsolete.toString() : "";
    historicRatePeriodString += (inverse != null) ? "&inverse=" + inverse.toString() : "";

    logger.debug("Calling {}", historicRatePeriodString);
    response = wsClient.getResponse(historicRatePeriodString, HistoricRatePeriod.class);
    return response;
  }

  /**
   * Returns a daily historic rate for a single base currency and one or more counter currencies
   * over a period of time
   *
   * @param from The currency you want to convert from
   * @param to Comma separated list of to currencies. Use * to convert to all currencies.
   * @param amount The amount you want to convert
   * @param start The starting timestamp in the UTC format yyyy-mm-ddThh:mm
   * @param end The ending timestamp in the UTC format yyyy-mm-ddThh:mm
   * @param interval Specifies daily or hourly data, interval is daily by default
   * @param page The page number to request. The default is the first page (page 1)
   * @param perPage The number of results per page. Default is 30 per page up to a maximum of 100
   *     per page
   * @param obsolete Specifies whether to return obsolete currencies
   * @return HistoricRatePeriodResponse object containing the daily historical rates data
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<HistoricRatePeriod> historicRatePeriod(
      String from,
      String to,
      Double amount,
      String start,
      String end,
      String interval,
      Integer page,
      Integer perPage,
      Boolean obsolete)
      throws XecdRatesException, IOException {
    return historicRatePeriod(
        from, to, amount, start, end, interval, page, perPage, obsolete, null);
  }

  /**
   * Returns monthly average rates for a single base currency and one or more counter currencies for
   * a year and optionally month
   *
   * @param from The currency you want to convert from
   * @param to Comma separated list of to currencies. Use * to convert to all currencies.
   * @param amount The amount you want to convert
   * @param year The specified year to calculate average monthly rates
   * @param month The specified month in given year to return average monthly rates
   * @param obsolete Specifies whether to return obsolete currencies
   * @param inverse Specifies whether to return inverse rates
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<MonthlyAverage> monthlyAverage(
      String from,
      String to,
      Double amount,
      Integer year,
      Integer month,
      Boolean obsolete,
      Boolean inverse)
      throws XecdRatesException, IOException {
    ResponseBean<MonthlyAverage> response = null;

    String monthlyAverageString =
        MessageFormat.format(monthlyAverageUrl, config.getServerPrefix(), to);
    monthlyAverageString += (from != null && !from.isEmpty()) ? "&from=" + from : "";
    monthlyAverageString += (amount != null && amount != 0) ? "&amount=" + amount : "";
    monthlyAverageString += (year != null) ? "&year=" + year : "";
    monthlyAverageString += (month != null) ? "&month=" + month : "";
    monthlyAverageString += (obsolete != null) ? "&obsolete=" + obsolete.toString() : "";
    monthlyAverageString += (inverse != null) ? "&inverse=" + inverse.toString() : "";

    logger.debug("Calling {}", monthlyAverageString);
    response = wsClient.getResponse(monthlyAverageString, MonthlyAverage.class);
    return response;
  }

  /**
   * Returns monthly average rates for a single base currency and one or more counter currencies for
   * a year and optionally month
   *
   * @param from The currency you want to convert from
   * @param to Comma separated list of to currencies. Use * to convert to all currencies.
   * @param amount The amount you want to convert
   * @param year The specified year to calculate average monthly rates
   * @param month The specified month in given year to return average monthly rates
   * @param obsolete Specifies whether to return obsolete currencies
   * @param inverse Specifies whether to return inverse rates
   * @throws IOException If there are network errors, or problems serializing/deserializing the
   *     request/response.
   * @throws XecdRatesException If the XECD rates api returns an error, or if there is an error in
   *     configuration
   */
  public ResponseBean<MonthlyAverage> monthlyAverage(
      String from, String to, Double amount, Integer year, Integer month, Boolean obsolete)
      throws XecdRatesException, IOException {
    return monthlyAverage(from, to, amount, year, month, obsolete, null);
  }
}
