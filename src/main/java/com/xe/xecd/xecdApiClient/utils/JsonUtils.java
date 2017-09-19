package com.xe.xecd.xecdApiClient.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.xe.xecd.xecdApiClient.exception.ErrorResponse;
import com.xe.xecd.xecdApiClient.mixins.AccountMixin;
import com.xe.xecd.xecdApiClient.mixins.ConvertMixin;
import com.xe.xecd.xecdApiClient.mixins.CurrencyMixin;
import com.xe.xecd.xecdApiClient.mixins.ErrorResponseMixin;
import com.xe.xecd.xecdApiClient.mixins.RateMixin;
import com.xe.xecd.xecdApiClient.mixins.MonthlyAverageRateMixin;
import com.xe.xecd.xecdApiClient.model.Account;
import com.xe.xecd.xecdApiClient.model.ConvertFrom;
import com.xe.xecd.xecdApiClient.model.ConvertTo;
import com.xe.xecd.xecdApiClient.model.Currency;
import com.xe.xecd.xecdApiClient.model.Conversion;
import com.xe.xecd.xecdApiClient.model.MonthlyAverageRate;

public class JsonUtils {
  private ObjectMapper mapper;

  public JsonUtils(){
    mapper = new ObjectMapper();
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    mapper.addMixIn(Account.class, AccountMixin.class);
    mapper.addMixIn(ConvertFrom.class, ConvertMixin.class);
    mapper.addMixIn(ConvertTo.class, ConvertMixin.class);
    mapper.addMixIn(Currency.class, CurrencyMixin.class);
    mapper.addMixIn(Conversion.class, RateMixin.class);
    mapper.addMixIn(MonthlyAverageRate.class, MonthlyAverageRateMixin.class);
    mapper.addMixIn(ErrorResponse.class, ErrorResponseMixin.class);
  }

  public ObjectMapper getMapper() {
    return mapper;
  }

  public <T> T fromJson(String json, Class<T> clazz)
      throws JsonParseException, JsonMappingException, IOException {
    return mapper.readValue(json, clazz);
  }

  public String toJson(Object obj) throws JsonProcessingException {
    return mapper.writeValueAsString(obj);
  }
}
