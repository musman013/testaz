package com.nfinity.ll.testaz.application.vets.dto;

import java.util.Date;
public class FindVetsByIdOutput {

  private String firstName;
  private Integer id;
  private String lastName;

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
  
 
}
