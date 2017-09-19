package com.xe.xecd.xecdApiClient.service;

public class ParameterDefinition {
  private boolean required = false;
  private boolean secret = false;
  private String environmentVariableName;
  private String optionGetterName;
  private String defaultValue;

  public boolean isRequired() {
    return required;
  }

  public boolean isSecret() {
    return secret;
  }

  public String getEnvironmentVariableName() {
    return environmentVariableName;
  }

  public String getOptionName() {
    return optionGetterName;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public ParameterDefinition required() {
    this.required = true;
    return this;
  }

  public ParameterDefinition secret() {
    this.secret = true;
    return this;
  }

  public ParameterDefinition withEnvironmentVariableName(String variableName) {
    this.environmentVariableName = variableName;
    return this;
  }

  public ParameterDefinition withOptionName(String variableName) {
    this.optionGetterName = variableName;
    return this;
  }

  public ParameterDefinition withDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }
}
