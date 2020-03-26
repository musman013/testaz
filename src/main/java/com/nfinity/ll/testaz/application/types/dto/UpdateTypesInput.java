package com.nfinity.ll.testaz.application.types.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateTypesInput {

  @NotNull(message = "id Should not be null")
  private Integer id;
  @Length(max = 80, message = "name must be less than 80 characters")
  private String name;

 
  public Integer getId() {
  	return id;
  }

  public void setId(Integer id){
  	this.id = id;
  }
 
  public String getName() {
  	return name;
  }

  public void setName(String name){
  	this.name = name;
  }
}
