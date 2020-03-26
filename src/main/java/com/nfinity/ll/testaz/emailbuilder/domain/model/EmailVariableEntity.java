package com.nfinity.ll.testaz.emailbuilder.domain.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EmailVariable")
public class EmailVariableEntity implements Serializable {
	

    private Long id;
    private String propertyName;
    private String propertyType;
    private String defaultValue;


	public EmailVariableEntity() {
	}

	public EmailVariableEntity(String propertyName, String propertyType, String defaultValue) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.defaultValue = defaultValue;
	}
	
    @Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "PropertyName", nullable = false ,length = 50)
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
    
	@Basic
	@Column(name = "PropertyType", nullable = false ,length = 50)
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
    
	@Basic
	@Column(name = "DefaultValue", nullable = true ,length = 100)
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}