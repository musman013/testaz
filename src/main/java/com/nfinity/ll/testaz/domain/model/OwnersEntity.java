package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "owners", schema = "sample")
public class OwnersEntity implements Serializable {

  	private String address;
  	private String city;
  	private String firstName;
  	private Integer id;
  	private String lastName;
  	private String telephone;
 
  	public OwnersEntity() {
  	}

  @Basic
  @Column(name = "address", nullable = true, length =255)
  public String getAddress() {
  return address;
  }

  public void setAddress(String address) {
  this.address = address;
  }
  
  @Basic
  @Column(name = "city", nullable = true, length =80)
  public String getCity() {
  return city;
  }

  public void setCity(String city) {
  this.city = city;
  }
  
  @Basic
  @Column(name = "firstName", nullable = true, length =30)
  public String getFirstName() {
  return firstName;
  }

  public void setFirstName(String firstName) {
  this.firstName = firstName;
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
  @Column(name = "lastName", nullable = true, length =30)
  public String getLastName() {
  return lastName;
  }

  public void setLastName(String lastName) {
  this.lastName = lastName;
  }
  
  @OneToMany(mappedBy = "owners", cascade = CascadeType.ALL) 
  public Set<PetsEntity> getPetsSet() { 
      return petsSet; 
  } 
 
  public void setPetsSet(Set<PetsEntity> pets) { 
      this.petsSet = pets; 
  } 
 
  private Set<PetsEntity> petsSet = new HashSet<PetsEntity>(); 
  @Basic
  @Column(name = "telephone", nullable = true, length =20)
  public String getTelephone() {
  return telephone;
  }

  public void setTelephone(String telephone) {
  this.telephone = telephone;
  }
  

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof OwnersEntity)) return false;
//        OwnersEntity owners = (OwnersEntity) o;
//        return id != null && id.equals(owners.id);
//  }

}

  
      


