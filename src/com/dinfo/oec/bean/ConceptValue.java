package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.List;

public class ConceptValue implements Serializable{
	private String id;
	private List<String> conceptValues;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getConceptValues() {
		return conceptValues;
	}
	public void setConceptValues(List<String> conceptValues) {
		this.conceptValues = conceptValues;
	}
	@Override
	public String toString() {
		return "ConceptValue [conceptValues=" + conceptValues + ", id=" + id
				+ "]";
	}
	
	
}
