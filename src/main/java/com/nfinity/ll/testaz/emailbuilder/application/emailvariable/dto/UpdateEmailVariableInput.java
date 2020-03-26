package com.nfinity.ll.testaz.emailbuilder.application.emailvariable.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateEmailVariableInput {

	@NotNull(message = "Id Should not be null")
    private Long id;
	
	@NotNull(message = "Property_Name Should not be null")
	@Length(max = 50, message = "Property_Name must be less than 50 characters")
	private String propertyName;
	
	@NotNull(message = "Property_Type Should not be null")
	@Length(max = 50, message = "Property_Type must be less than 50 characters")	
    private String propertyType;
	
	@Length(max = 100, message = "Default_Value must be less than 100 characters")
    private String defaultValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
