package com.nfinity.ll.testaz.application.authorization.userrole.dto;

import java.util.Date;

public class GetUserOutput {
  private Integer accessFailedCount;
  private String authenticationSource;
  private String emailAddress;
  private String emailConfirmationCode;
  private String firstName;
  private Long id;
  private Boolean isActive;
  private Boolean isEmailConfirmed;
  private Boolean isLockoutEnabled;
  private String isPhoneNumberConfirmed;
  private Date lastLoginTime;
  private String lastName;
  private Date lockoutEndDateUtc;
  private String password;
  private String passwordResetCode;
  private String phoneNumber;
  private Long profilePictureId;
  private Boolean twoFactorEnabled;
  private String userName;

  private Long UserroleRoleId;
  
  public Long getUserroleRoleId() {
  return UserroleRoleId;
  }

  public void setUserroleRoleId(Long UserroleRoleId){
  this.UserroleRoleId = UserroleRoleId;
  }
  private Long UserroleUserId;
  
  public Long getUserroleUserId() {
  return UserroleUserId;
  }

  public void setUserroleUserId(Long UserroleUserId){
  this.UserroleUserId = UserroleUserId;
  }
  public Integer getAccessFailedCount() {
  return accessFailedCount;
  }

  public void setAccessFailedCount(Integer accessFailedCount){
  this.accessFailedCount = accessFailedCount;
  }
  public String getAuthenticationSource() {
  return authenticationSource;
  }

  public void setAuthenticationSource(String authenticationSource){
  this.authenticationSource = authenticationSource;
  }
  public String getEmailAddress() {
  return emailAddress;
  }

  public void setEmailAddress(String emailAddress){
  this.emailAddress = emailAddress;
  }
  public String getEmailConfirmationCode() {
  return emailConfirmationCode;
  }

  public void setEmailConfirmationCode(String emailConfirmationCode){
  this.emailConfirmationCode = emailConfirmationCode;
  }
  public String getFirstName() {
  return firstName;
  }

  public void setFirstName(String firstName){
  this.firstName = firstName;
  }
  public Long getId() {
  return id;
  }

  public void setId(Long id){
  this.id = id;
  }
  public Boolean getIsActive() {
  return isActive;
  }

  public void setIsActive(Boolean isActive){
  this.isActive = isActive;
  }
  public Boolean getIsEmailConfirmed() {
  return isEmailConfirmed;
  }

  public void setIsEmailConfirmed(Boolean isEmailConfirmed){
  this.isEmailConfirmed = isEmailConfirmed;
  }
  public Boolean getIsLockoutEnabled() {
  return isLockoutEnabled;
  }

  public void setIsLockoutEnabled(Boolean isLockoutEnabled){
  this.isLockoutEnabled = isLockoutEnabled;
  }
  public String getIsPhoneNumberConfirmed() {
  return isPhoneNumberConfirmed;
  }

  public void setIsPhoneNumberConfirmed(String isPhoneNumberConfirmed){
  this.isPhoneNumberConfirmed = isPhoneNumberConfirmed;
  }
  public Date getLastLoginTime() {
  return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime){
  this.lastLoginTime = lastLoginTime;
  }
  public String getLastName() {
  return lastName;
  }

  public void setLastName(String lastName){
  this.lastName = lastName;
  }
  public Date getLockoutEndDateUtc() {
  return lockoutEndDateUtc;
  }

  public void setLockoutEndDateUtc(Date lockoutEndDateUtc){
  this.lockoutEndDateUtc = lockoutEndDateUtc;
  }
  public String getPassword() {
  return password;
  }

  public void setPassword(String password){
  this.password = password;
  }
  public String getPasswordResetCode() {
  return passwordResetCode;
  }

  public void setPasswordResetCode(String passwordResetCode){
  this.passwordResetCode = passwordResetCode;
  }
  public String getPhoneNumber() {
  return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber){
  this.phoneNumber = phoneNumber;
  }
  public Long getProfilePictureId() {
  return profilePictureId;
  }

  public void setProfilePictureId(Long profilePictureId){
  this.profilePictureId = profilePictureId;
  }
  public Boolean getTwoFactorEnabled() {
  return twoFactorEnabled;
  }

  public void setTwoFactorEnabled(Boolean twoFactorEnabled){
  this.twoFactorEnabled = twoFactorEnabled;
  }
  public String getUserName() {
  return userName;
  }

  public void setUserName(String userName){
  this.userName = userName;
  }

}
