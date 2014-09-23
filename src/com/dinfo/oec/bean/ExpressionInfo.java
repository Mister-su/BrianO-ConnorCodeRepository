package com.dinfo.oec.bean;

import java.util.ArrayList;
import java.util.List;

public class ExpressionInfo {
	
	private String expression;
	
	private List<String> matchinfo;
	
	private List<String> elements = new ArrayList<String>();
	
	private List<String> concepts = new ArrayList<String>();
	
//	private List<String> ontology =new ArrayList<String>();
	
	private double weight;
	
	
	public static ExpressionInfo newInstance()
	{
		return new ExpressionInfo();
	}
	
	public ExpressionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ExpressionInfo(String expression, List<String> matchinfo,
			List<String> elements, List<String> concepts,
			 double weight) {
		super();
		this.expression = expression;
		this.matchinfo = matchinfo;
		this.elements = elements;
		this.concepts = concepts;
//		this.ontology = ontology;
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	

	public void setMatchinfo(List<String> matchinfo) {
		this.matchinfo = matchinfo;
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

	
	
}
