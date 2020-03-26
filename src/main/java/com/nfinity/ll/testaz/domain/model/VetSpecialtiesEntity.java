package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "vet_specialties", schema = "sample")
@IdClass(VetSpecialtiesId.class)
public class VetSpecialtiesEntity implements Serializable {

  	private Integer specialtyId;
  	private Integer vetId;
 
  	public VetSpecialtiesEntity() {
  	}

  @ManyToOne
  @JoinColumn(name = "specialtyId", insertable=false, updatable=false)
  public SpecialtiesEntity getSpecialties() {
    return specialties;
  }
  public void setSpecialties(SpecialtiesEntity specialties) {
    this.specialties = specialties;
  }
  
  private SpecialtiesEntity specialties;
 
  	@Id
  	@Column(name = "specialtyId", nullable = false)
  public Integer getSpecialtyId() {
  return specialtyId;
  }

  public void setSpecialtyId(Integer specialtyId) {
  this.specialtyId = specialtyId;
  } 
  
  	@Id
  	@Column(name = "vetId", nullable = false)
  public Integer getVetId() {
  return vetId;
  }

  public void setVetId(Integer vetId) {
  this.vetId = vetId;
  } 
  
  @ManyToOne
  @JoinColumn(name = "vetId", insertable=false, updatable=false)
  public VetsEntity getVets() {
    return vets;
  }
  public void setVets(VetsEntity vets) {
    this.vets = vets;
  }
  
  private VetsEntity vets;
 

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof VetSpecialtiesEntity)) return false;
//        VetSpecialtiesEntity vetspecialties = (VetSpecialtiesEntity) o;
//        return id != null && id.equals(vetspecialties.id);
//  }

}

  
      


