package com.nfinity.ll.testaz.application.vets.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateVetsInput {

  @Length(max = 30, message = "firstName must be less than 30 characters")
  private String firstName;
  @NotNull(message = "id Should not be null")
  private Integer id;
  @Length(max = 30, message = "lastName must be less than 30 characters")
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
