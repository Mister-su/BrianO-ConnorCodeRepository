package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.List;

public class ElementValue implements Serializable{
	private String id;
	private List<String> elementValues;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getElementValues() {
		return elementValues;
	}
	public void setElementValues(List<String> elementValues) {
		this.elementValues = elementValues;
	}
	@Override
	public String toString() {
		return "ElementValue [elementValues=" + elementValues + ", id=" + id
				+ "]";
	}
	
	
}
