package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "visits", schema = "sample")
public class VisitsEntity implements Serializable {

  	private String description;
  	private Integer id;
  	private Date visitDate;
 
  	public VisitsEntity() {
  	}

  @Basic
  @Column(name = "description", nullable = true, length =255)
  public String getDescription() {
  return description;
  }

  public void setDescription(String description) {
  this.description = description;
  }
  
  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
  public Integer getId() {
  return id;
  }

  public void setId(Integer id) {
  this.id = id;
  } 
  
  @ManyToOne
  @JoinColumn(name = "petId")
  public PetsEntity getPets() {
    return pets;
  }
  public void setPets(PetsEntity pets) {
    this.pets = pets;
  }
  
  private PetsEntity pets;
 
  @Basic
  @Column(name = "visitDate", nullable = true)
  public Date getVisitDate() {
  return visitDate;
  }

  public void setVisitDate(Date visitDate) {
  this.visitDate = visitDate;
  }
  

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof VisitsEntity)) return false;
//        VisitsEntity visits = (VisitsEntity) o;
//        return id != null && id.equals(visits.id);
//  }

}

  
      


