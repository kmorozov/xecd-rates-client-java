package com.xe.xecd.xecdApiClient.model;

import java.util.Date;

public class Account {
  private String id;
  private String organization;
  private String productPackage;
  private Date serviceStartDate;
  private String packageLimitDuration;
  private Integer packageLimit;
  private Integer packageLimitRemaining;
  private Date packageLimitResetDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }	

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public Date getServiceStartTimestamp() {
    return serviceStartDate;
  }

  public void setServiceStartDate(Date serviceStartDate) {
    this.serviceStartDate = serviceStartDate;
  }

  public String getPackage() {
    return productPackage;
  }

  public void setPackage(String productPackage) {
    this.productPackage = productPackage;
  }

  public String getPackageLimitDuration() {
    return packageLimitDuration;
  }

  public void setPackageLimitDuration(String packageLimitDuration) {
    this.packageLimitDuration = packageLimitDuration;
  }

  public Integer getPackageLimit() {
    return packageLimit;
  }

  public void setPackageLimit(Integer packageLimit) {
    this.packageLimit = packageLimit;
  }

  public Integer getPackageLimitRemaining() {
    return packageLimitRemaining;
  }

  public void setPackageLimitRemaining(Integer packageLimitRemaining) {
    this.packageLimitRemaining = packageLimitRemaining;
  }

  public Date getPackageLimitReset() {
    return packageLimitResetDate;
  }

  public void setPackageLimitResetDate(Date packageLimitResetDate) {
    this.packageLimitResetDate = packageLimitResetDate;
  }
}
