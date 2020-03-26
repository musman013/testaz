package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;

public class VetSpecialtiesId implements Serializable {

    private Integer specialtyId;
    private Integer vetId;

    public VetSpecialtiesId() {
    }
    
    public VetSpecialtiesId(Integer specialtyId,Integer vetId) {
  		this.specialtyId =specialtyId;
  		this.vetId =vetId;
    }
    
    public Integer getSpecialtyId() {
        return specialtyId;
    }
    public void setSpecialtyId(Integer specialtyId){
        this.specialtyId = specialtyId;
    }
    public Integer getVetId() {
        return vetId;
    }
    public void setVetId(Integer vetId){
        this.vetId = vetId;
    }
    
}