package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "pets", schema = "sample")
public class PetsEntity implements Serializable {

  	private Date birthDate;
  	private Integer id;
  	private String name;
 
  	public PetsEntity() {
  	}

  @Basic
  @Column(name = "birthDate", nullable = true)
  public Date getBirthDate() {
  return birthDate;
  }

  public void setBirthDate(Date birthDate) {
  this.birthDate = birthDate;
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
  
  @Basic
  @Column(name = "name", nullable = true, length =30)
  public String getName() {
  return name;
  }

  public void setName(String name) {
  this.name = name;
  }
  
  @ManyToOne
  @JoinColumn(name = "ownerId")
  public OwnersEntity getOwners() {
    return owners;
  }
  public void setOwners(OwnersEntity owners) {
    this.owners = owners;
  }
  
  private OwnersEntity owners;
 
  @ManyToOne
  @JoinColumn(name = "typeId")
  public TypesEntity getTypes() {
    return types;
  }
  public void setTypes(TypesEntity types) {
    this.types = types;
  }
  
  private TypesEntity types;
 
  @OneToMany(mappedBy = "pets", cascade = CascadeType.ALL) 
  public Set<VisitsEntity> getVisitsSet() { 
      return visitsSet; 
  } 
 
  public void setVisitsSet(Set<VisitsEntity> visits) { 
      this.visitsSet = visits; 
  } 
 
  private Set<VisitsEntity> visitsSet = new HashSet<VisitsEntity>(); 

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof PetsEntity)) return false;
//        PetsEntity pets = (PetsEntity) o;
//        return id != null && id.equals(pets.id);
//  }

}

  
      


