package com.panache.pojo;

public class DropDownList {
	private String value;
	private String label;
	
	public DropDownList(String value, String label) {
		super();
		this.value = value;
		this.label = label;
	}

	public DropDownList() {
		super();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "DropDownList [value=" + value + ", label=" + label + "]";
	}
	
}
