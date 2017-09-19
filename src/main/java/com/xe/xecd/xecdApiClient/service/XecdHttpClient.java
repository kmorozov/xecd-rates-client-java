package com.xe.xecd.xecdApiClient.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xe.xecd.xecdApiClient.exception.ErrorResponse;
import com.xe.xecd.xecdApiClient.exception.XecdRatesException;
import com.xe.xecd.xecdApiClient.model.ResponseBean;
import com.xe.xecd.xecdApiClient.utils.JsonUtils;

public class XecdHttpClient {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private HttpClient client = null;
  private JsonUtils jsonUtils = new JsonUtils();
  
  private String username;
  private String password;

  public XecdHttpClient(HttpClient client, String username, String password) {
    this.client = client;
    this.username = username;
    this.password = password;
  }

  public <T> ResponseBean<T> getResponse(
      String url, Class<T> returnType)
      throws XecdRatesException, IOException {
    HttpGet get;
    try {
      get = new HttpGet(new URI(url));
    } catch (URISyntaxException e) {
      throw new XecdRatesException(
          "Uri was malformed. This is likely due to an incorrect server prefix provided in the config.",
          e);
    }
    Base64 base64 = new Base64();
    String encodedUsernamePassword = base64.encodeAsString((username + ":" + password).getBytes());

    get.setHeader("Authorization", "Basic " + encodedUsernamePassword);

    if (logger.isDebugEnabled()) {
      logger.debug("request = {}", jsonUtils.toJson(get));
    }

    HttpResponse response = client.execute(get);

    HttpEntity bodyEntity = response.getEntity();

    String responseString = EntityUtils.toString(bodyEntity);

    logger.debug("response = {}", responseString);

    if (response.getStatusLine().getStatusCode() != 200) {
      ErrorResponse errorResponse = jsonUtils.fromJson(responseString, ErrorResponse.class);
      throw new XecdRatesException(errorResponse, response.getStatusLine().getStatusCode());
    }

    ResponseBean<T> responseBean = new ResponseBean<T>();
    for (Header header : response.getAllHeaders()) {
      responseBean.addHeader(header.getName(), header.getValue());
    }
    responseBean.setData(jsonUtils.fromJson(responseString, returnType));
    return responseBean;
  }
}
