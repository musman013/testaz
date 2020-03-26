package com.nfinity.ll.testaz.application.owners.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CreateOwnersInput {

  private String address;
  
  @Length(max = 80, message = "city must be less than 80 characters")
  private String city;
  
  @Length(max = 30, message = "firstName must be less than 30 characters")
  private String firstName;
  
  @Length(max = 30, message = "lastName must be less than 30 characters")
  private String lastName;
  
  @Length(max = 20, message = "telephone must be less than 20 characters")
  private String telephone;
  

  public String getAddress() {
  return address;
  }

  public void setAddress(String address){
  this.address = address;
  }
  
  public String getCity() {
  return city;
  }

  public void setCity(String city){
  this.city = city;
  }
  
  public String getFirstName() {
  return firstName;
  }

  public void setFirstName(String firstName){
  this.firstName = firstName;
  }
  
  public String getLastName() {
  return lastName;
  }

  public void setLastName(String lastName){
  this.lastName = lastName;
  }
  
  public String getTelephone() {
  return telephone;
  }

  public void setTelephone(String telephone){
  this.telephone = telephone;
  }
  
 
}
