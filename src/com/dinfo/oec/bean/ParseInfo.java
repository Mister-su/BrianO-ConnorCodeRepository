package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

public class ParseInfo implements Serializable{
	
	private List<String> catlogs = new ArrayList<String>();
	private List<Integer> intentions = Lists.newArrayList();
	private Set<String> matchInfos = new HashSet<String>();
	private List<String> concepts = new ArrayList<String>();
	private List<String> elements = new ArrayList<String>();
	private List<OntologyInfo> ontologyInfos = new ArrayList<OntologyInfo>();
	private List<String> catlognames = new ArrayList<String>();
	private List<String> expressions  =new ArrayList<String>();
	private List<String> segwords = new ArrayList<String>();

	private List<Double> weights = new ArrayList<Double>();
	
	private List<String> catlogPath = new ArrayList<String>();

	private boolean status;
	
	private List<String> inner_code = new ArrayList<String>();
	
	private List<String> shared = new ArrayList<String>();
	
	
	private List<ConceptValue> conceptValueList;
	
	private List<ElementValue> elementValueList;
	
	private List<Double> factors = new ArrayList<Double>();
	
	/**
	 * 元素值和概念值返回的map集合
	 */
	private Map<String,ArrayList<String>> conceptValueMap;
	
	private Map<String,ArrayList<String>> elementValueMap;
	
	
	public List<Double> getFactors() {
		return factors;
	}

	public void setFactors(List<Double> factors) {
		this.factors = factors;
	}

	public List<ConceptValue> getConceptValueList() {
		return conceptValueList;
	}

	public void setConceptValueList(List<ConceptValue> conceptValueList) {
		this.conceptValueList = conceptValueList;
	}

	public List<ElementValue> getElementValueList() {
		return elementValueList;
	}

	public void setElementValueList(List<ElementValue> elementValueList) {
		this.elementValueList = elementValueList;
	}

	public List<String> getConcepts() {
		return concepts;
	}

	public List<String> getElements() {
		return elements;
	}

	public List<OntologyInfo> getOntologyInfos() {
		return ontologyInfos;
	}

	public void setOntologyInfos(List<OntologyInfo> ontologyInfos) {
		this.ontologyInfos = ontologyInfos;
	}

	public List<String> getCatlognames() {
		return catlognames;
	}

	public void setCatlognames(List<String> catlognames) {
		this.catlognames = catlognames;
	}

	public List<String> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<String> expressions) {
		this.expressions = expressions;
	}

	public void setConcepts(List<String> concepts) {
		this.concepts = concepts;
	}

	public void setElements(List<String> elements) {
		this.elements = elements;
	}

	public List<String> getInner_code() {
		return inner_code;
	}

	public void setInner_code(List<String> innerCode) {
		inner_code = innerCode;
	}


	public List<String> getShared() {
		return shared;
	}

	public void setShared(List<String> shared) {
		this.shared = shared;
	}

	private String id;
	
	public List<String> getSegwords() {
		return segwords;
	}

	public void setSegwords(List<String> segwords) {
		this.segwords = segwords;
	}

	public List<String> getCatlogPath() {
		return catlogPath;
	}

	public void setCatlogPath(List<String> catlogPath) {
		this.catlogPath = catlogPath;
	}

	// @Property
	// private HashMap<String,ArrayList<String>> elementValues ;
	// @Property
	// private HashMap<String,ArrayList<String>> conceptValues;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// public HashMap<String, ArrayList<String>> getElementValues() {
	// return elementValues;
	// }
	//
	// public void setElementValues(HashMap<String, ArrayList<String>>
	// elementValues) {
	// this.elementValues = elementValues;
	// }
	//
	// public HashMap<String, ArrayList<String>> getConceptValues() {
	// return conceptValues;
	// }
	//
	// public void setConceptValues(HashMap<String, ArrayList<String>>
	// conceptValues) {
	// this.conceptValues = conceptValues;
	// }

	
	

	

	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

//	public List<OntologyInfo> getOntologyInfos() {
//		return ontologyInfos;
//	}
//
//	public void setOntologyInfos(List<OntologyInfo> ontologyInfos) {
//		this.ontologyInfos = ontologyInfos;
//	}


	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<String> getCatlogs() {

		return catlogs;
	}

	public void setCatlogs(List<String> catlogs) {
		this.catlogs = catlogs;
	}

	public Set<String> getMatchInfos() {
		return matchInfos;
	}

	public void setMatchInfos(Set<String> matchInfos) {
		this.matchInfos = matchInfos;
	}


	public void setConcepts(ArrayList<String> concepts) {
		this.concepts = concepts;
	}


	public void setElements(ArrayList<String> elements) {
		this.elements = elements;
	}


	public List<Integer> getIntentions() {
		return intentions;
	}

	public void setIntentions(List<Integer> intentions) {
		this.intentions = intentions;
	}

	
	@Override
	public String toString() {
		return "ParseInfo [catlogPath=" + catlogPath + ", catlognames="
				+ catlognames + ", catlogs=" + catlogs + ", conceptValueList="
				+ conceptValueList + ", concepts=" + concepts
				+ ", elementValueList=" + elementValueList + ", elements="
				+ elements + ", expressions=" + expressions + ", factors="
				+ factors + ", id=" + id + ", inner_code=" + inner_code
				+ ", intentions=" + intentions + ", matchInfos=" + matchInfos
				+ ", ontologyInfos=" + ontologyInfos + ", segwords=" + segwords
				+ ", shared=" + shared + ", status=" + status + ", weights="
				+ weights + "]";
	}
	
	

	
	
	

	
	public Map<String, ArrayList<String>> getConceptValueMap() {
		return conceptValueMap;
	}

	public void setConceptValueMap(Map<String, ArrayList<String>> conceptValueMap) {
		this.conceptValueMap = conceptValueMap;
	}

	public Map<String, ArrayList<String>> getElementValueMap() {
		return elementValueMap;
	}

	public void setElementValueMap(Map<String, ArrayList<String>> elementValueMap) {
		this.elementValueMap = elementValueMap;
	}
	


	
	
	
	
}
