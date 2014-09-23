package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OntologyInfo implements Serializable{
	
	
	private String ontoid;
	
	private String ontoname;
	
	private String ontopath;
	
	private double weight;
	
	private  String  inner_code;
	
	private  String shared ;
	
	private  int priority;
	
	private double factor;
	
	private List<Integer> ids = new ArrayList<Integer>();
	
	private Set<String> expressions = new HashSet<String>();
	
	
	private int pfilter;

//	private List<ExpressionInfo> expressionInfos = new ArrayList<ExpressionInfo>();
	
	public static OntologyInfo newInstance()
	{
		return new OntologyInfo();
	}
	
	public OntologyInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OntologyInfo(String ontoid, String ontoname, String ontopath,
			double weight ,Collection<String> expressions ,int i) {
		super();
		this.ontoid = ontoid;
		this.ontoname = ontoname;
		this.ontopath = ontopath;
		this.weight = weight;
		this.expressions.addAll(expressions);
		if(ParseConfig.pfilters.containsKey(ontoid))
			this.pfilter = ParseConfig.pfilters.get(ontoid);
		this.ids.add(i);
	}

	

	


	public OntologyInfo(String ontoid, String ontoname, String ontopath,
			double weight, String innerCode, String shared ,Collection<String> expressions ,int i) {
		super();
		this.ontoid = ontoid;
		this.ontoname = ontoname;
		this.ontopath = ontopath;
		this.weight = weight;
		inner_code = innerCode;
		this.shared = shared;
		this.expressions.addAll(expressions);
		this.ids.add(i);
		if(ParseConfig.pfilters.containsKey(ontoid))
			this.pfilter = ParseConfig.pfilters.get(ontoid);
	}
	
	public OntologyInfo(String ontoid, String ontoname, String ontopath,
			double weight, String innerCode, String shared ,double factor,Collection<String> expressions ,int i ) {
		super();
		this.ontoid = ontoid;
		this.ontoname = ontoname;
		this.ontopath = ontopath;
		this.weight = weight;
		this.factor=factor;
		inner_code = innerCode;
		this.shared = shared;
		
		this.expressions.addAll(expressions);
		this.ids.add(i);
		if(ParseConfig.pfilters.containsKey(ontoid))
			this.pfilter = ParseConfig.pfilters.get(ontoid);
	}
	
	

	public OntologyInfo(String ontoid, String ontoname, String ontopath,
			double weight, String innerCode, String shared,double factor,
			Collection<String> expressions ) {
		super();
		this.ontoid = ontoid;
		this.ontoname = ontoname;
		this.ontopath = ontopath;
		this.weight = weight;
		inner_code = innerCode;
		this.shared = shared;
		this.factor=factor;
		this.expressions.addAll(expressions);
		if(ParseConfig.pfilters.containsKey(ontoid))
			this.pfilter = ParseConfig.pfilters.get(ontoid);
	}


	
	
	public int getPfilter() {
		return pfilter;
	}

	public void setPfilter(int pfilter) {
		this.pfilter = pfilter;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getInner_code() {
		return inner_code;
	}

	public void setInner_code(String innerCode) {
		inner_code = innerCode;
	}

	public String getShared() {
		return shared;
	}

	public void setShared(String shared) {
		this.shared = shared;
	}

	public Set<String> getExpressions() {
		return expressions;
	}

	public void setExpressions(Set<String> expressions) {
		this.expressions = expressions;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public String getOntoid() {
		return ontoid;
	}

	public void setOntoid(String ontoid) {
		if(ParseConfig.pfilters.containsKey(ontoid))
			this.pfilter = ParseConfig.pfilters.get(ontoid);
		this.ontoid = ontoid;
	}

	public String getOntoname() {
		return ontoname;
	}

	public void setOntoname(String ontoname) {
		this.ontoname = ontoname;
	}

	public String getOntopath() {
		return ontopath;
	}

	public void setOntopath(String ontopath) {
		this.ontopath = ontopath;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

//	public List<ExpressionInfo> getExpressionInfos() {
//		return expressionInfos;
//	}
//
//	public void setExpressionInfos(List<ExpressionInfo> expressionInfos) {
//		this.expressionInfos = expressionInfos;
//	}

	@Override
	public boolean equals(Object obj) {
		OntologyInfo info =(OntologyInfo) obj;
		
		return info.getOntoid().equals(ontoid);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return ontoid.hashCode();
	}

	@Override
	public String toString() {
		return "OntologyInfo [expressions=" + expressions + ", factor="
				+ factor + ", ids=" + ids + ", inner_code=" + inner_code
				+ ", ontoid=" + ontoid + ", ontoname=" + ontoname
				+ ", ontopath=" + ontopath + ", pfilter=" + pfilter
				+ ", priority=" + priority + ", shared=" + shared + ", weight="
				+ weight + "]";
	}


	
	
	
	
}
