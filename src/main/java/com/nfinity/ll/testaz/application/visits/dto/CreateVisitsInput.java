package com.nfinity.ll.testaz.application.visits.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CreateVisitsInput {

  private String description;
  
  private Date visitDate;
  
  private Integer petId;

  public Integer getPetId() {
  	return petId;
  }

  public void setPetId(Integer petId){
  	this.petId = petId;
  }
  public String getDescription() {
  return description;
  }

  public void setDescription(String description){
  this.description = description;
  }
  
  public Date getVisitDate() {
  return visitDate;
  }

  public void setVisitDate(Date visitDate){
  this.visitDate = visitDate;
  }
  
 
}
