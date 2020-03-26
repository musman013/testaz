package com.nfinity.ll.testaz.emailbuilder.emailconverter.dto.request;

public class Height {

	private String unit = "";

	private String value = "";

	private boolean auto = false;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

}
