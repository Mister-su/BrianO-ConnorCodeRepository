package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusMsg implements Serializable{
	
	
	
	private List<String>  matchInfos = new ArrayList<String>();

	private List<String> elements = new ArrayList<String>();
	
	private List<String> concepts = new ArrayList<String>();
	
	private List<String> ontology =new ArrayList<String>();
	
	private List<String> matchwords=new ArrayList<String>();
	
	public int expresscount;
	


	public int elementcount=0;
	
	public int conceptcount=0;
	
	public int keycount=0;
	
	public int ontocount=0;
	
	public  boolean stackflag =false;
	
	public  String stackstr="";
	
	private String exp;
	

	
	public List<String> getMatchwords() {
		return matchwords;
	}

	public void setMatchwords(List<String> matchwords) {
		this.matchwords = matchwords;
	}

	public List<String> getOntology() {
		return ontology;
	}

	public void setOntology(List<String> ontology) {
		this.ontology = ontology;
	}
	
	public int getElementcount() {
		return elementcount;
	}

	public void setElementcount(int elementcount) {
		this.elementcount = elementcount;
	}

	public int getConceptcount() {
		return conceptcount;
	}

	public void setConceptcount(int conceptcount) {
		this.conceptcount = conceptcount;
	}

	public int getKeycount() {
		return keycount;
	}

	public void setKeycount(int keycount) {
		this.keycount = keycount;
	}

	public int getOntocount() {
		return ontocount;
	}

	public void setOntocount(int ontocount) {
		this.ontocount = ontocount;
	}


	
	public List<String> getElements() {
		return elements;
	}

	public void setElements(List<String> elements) {
		this.elements = elements;
	}


	public List<String> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<String> concepts) {
		this.concepts = concepts;
	}

	public List<String> getMatchInfos() {
		return matchInfos;
	}

	public void setMatchInfos(List<String> matchInfos) {
		this.matchInfos = matchInfos;
	}

	@Override
	public String toString() {
		return "StatusMsg [concepts=" + concepts + ", elements=" + elements
				+ ", matchInfos=" + matchInfos + "]\r\n";
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	

	
	
	
	
}
