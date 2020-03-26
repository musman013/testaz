package com.nfinity.ll.testaz.application.pets.dto;

import java.util.Date;

public class GetOwnersOutput {
  private String address;
  private String city;
  private String firstName;
  private Integer id;
  private String lastName;
  private String telephone;

  private Integer petsId;
  
  public Integer getPetsId() {
  	return petsId;
  }

  public void setPetsId(Integer petsId){
  	this.petsId = petsId;
  }
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
  public Integer getId() {
  	return id;
  }

  public void setId(Integer id){
  	this.id = id;
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
