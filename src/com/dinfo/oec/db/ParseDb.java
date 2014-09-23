package com.dinfo.oec.db;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.map.MultiKeyMap;

import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.MultiBTree;
import com.google.common.collect.Multimap;

public interface ParseDb {

	public abstract Multimap<String, String> getConceptResByType(int type);

	public abstract Multimap<String, String> getElementResByType(int type);

	public abstract BidiMap getOntologyMap();

	public abstract Map<String, String> getOntologyPath();

	public abstract Map<String, Integer> getElementWeight();

	public abstract Set getAllResource();

	public abstract Multimap<String, Integer> getIntentionExp();

	public abstract Multimap<String, String> getAllOntoExpresssion();

	public abstract Multimap<String, String> geOntolongyElementMap();

	public abstract BidiMap getAllConceptMap();

	public abstract BidiMap getAllElementMap();

	public abstract Map<String, String> getOntoDefaultCate();

	/**
	 * 加载所有的 模式
	 * @return
	 */
	public abstract Multimap<String, String> getAllOntoIntentionExp(String ontId);

	/**
	 * 加载所有的 模式
	 * @return
	 */
	public abstract Multimap<String, String> getAllOntoExp();

	public abstract Map<String, Integer> getOntoWeight();

	public abstract List<JobItem> getJobItemByJobid(String jobid);

	public abstract String getFilterWord();

	public abstract List<Integer> getintentionValue(List<String[]> list);

	/**
	 * 获取全部概念名
	 * @return
	 */
	public abstract List<String> getConceptName();

	/**
	 * 获取全部要素名
	 * @return
	 */
	public abstract List<String> getElementName();

	/**
	 * 根据条件查询表达式
	 * @param name
	 * @return
	 */
	public abstract List<String> getOntologyExpression();
	
	public MultiBTree getOntoLevelTree();

	public abstract Multimap<String, String> getAllOntoResource();
	
	public abstract List<String> getWordbase();
	
	public abstract List<String> getWorduserByType(int type);

	public abstract Map<String, String> getInnerCodes();

	public abstract Map<String, String> getOntoNames();

	public abstract MultiKeyMap getAllOntoResFactors();

	public abstract Map<String, Integer> getAllpfilters();
	
}