package com.nfinity.ll.testaz.application.authorization.user.dto;

import java.sql.Date;

public class CreateUserOutput {

    private Long id;
    private int accessFailedCount;
    private String emailAddress;
    private String emailConfirmationCode;
    private Boolean isEmailConfirmed;
    private Boolean isLockoutEnabled;
    private Boolean isPhoneNumberConfirmed;
    private Date lastLoginTime;
    private Date lockoutEndDateUtc;
    private String firstName;
    private Boolean isActive;
    private String passwordResetCode;
    private Boolean shouldChangePasswordOnNextLogin;
    private String phoneNumber;
    private Long profilePictureId;
    private String signInToken;
    private Date signInTokenExpireTimeUtc;
    private String lastName;
    private String userName;
    
    public CreateUserOutput() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAccessFailedCount() {
        return accessFailedCount;
    }

    public void setAccessFailedCount(int accessFailedCount) {
        this.accessFailedCount = accessFailedCount;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailConfirmationCode() {
        return emailConfirmationCode;
    }

    public void setEmailConfirmationCode(String emailConfirmationCode) {
        this.emailConfirmationCode = emailConfirmationCode;
    }

    public Boolean getIsEmailConfirmed() {
        return isEmailConfirmed;
    }

    public void setIsEmailConfirmed(Boolean emailConfirmed) {
        isEmailConfirmed = emailConfirmed;
    }

    public Boolean getIsLockoutEnabled() {
        return isLockoutEnabled;
    }

    public void setIsLockoutEnabled(Boolean lockoutEnabled) {
        isLockoutEnabled = lockoutEnabled;
    }

    public Boolean getIsPhoneNumberConfirmed() {
        return isPhoneNumberConfirmed;
    }

    public void setIsPhoneNumberConfirmed(Boolean phoneNumberConfirmed) {
        isPhoneNumberConfirmed = phoneNumberConfirmed;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLockoutEndDateUtc() {
        return lockoutEndDateUtc;
    }

    public void setLockoutEndDateUtc(Date lockoutEndDateUtc) {
        this.lockoutEndDateUtc = lockoutEndDateUtc;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
	public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
    
    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }
    
    public Boolean isShouldChangePasswordOnNextLogin() {
        return shouldChangePasswordOnNextLogin;
    }

    public void setShouldChangePasswordOnNextLogin(Boolean shouldChangePasswordOnNextLogin) {
        this.shouldChangePasswordOnNextLogin = shouldChangePasswordOnNextLogin;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(Long profilePictureId) {
        this.profilePictureId = profilePictureId;
    }

    public String getSignInToken() {
        return signInToken;
    }

    public void setSignInToken(String signInToken) {
        this.signInToken = signInToken;
    }

    public Date getSignInTokenExpireTimeUtc() {
        return signInTokenExpireTimeUtc;
    }

    public void setSignInTokenExpireTimeUtc(Date signInTokenExpireTimeUtc) {
        this.signInTokenExpireTimeUtc = signInTokenExpireTimeUtc;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
   
}
