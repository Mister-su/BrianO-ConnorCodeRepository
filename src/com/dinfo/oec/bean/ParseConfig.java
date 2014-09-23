package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

import com.chenlb.mmseg4j.example.MaxWord;
import com.chenlb.mmseg4j.example.Simple;
import com.google.common.collect.Multimap;


public class ParseConfig implements Serializable{
	
	/**
	 * 概念的关键词资源
	 */
	public static Multimap<String, String> conceptKeyRes;
	
	/**
	 * 概念的正则表达式资源    如 {中(.*)过  ,}
	 */
	public static Multimap<String, String> conceptRegRes;
	
	/**
	 * 概念name 和cateid 的对应关系 双向的 {中国<==>1_001,}
	 */
	public static BidiMap conceptMap;
	
	/**
	 * 要素的关键词资源
	 */
	public static Multimap<String, String> elementKeyRes;
	
	
	public static Map<String,Integer> elementWeight;
	
	/**
	 * 要素的正则表达式资源
	 */
	public static Multimap<String, String> elementRegRes;
	
	
	/**
	 * 
	 * 要素catename 和cateid 对应关系 {中国=1_100,美国=1_100}
	 */
	public static BidiMap elementMap;
	
	
	/**
	 * 本体类型和本体的对应关系 ,名字和 id的对应关系
	 */
	public static BidiMap ontologyMap;
	
	/**
	 * 本体类型和本体的路径关系
	 */
	public static Map<String,String> ontologyPath;
	
	/**
	 * 表里面的所有本体表达式和本体类的对应关系  如{ k_加强={1_1001,2_1004},k_中国={2_2001,3_3001}}
	 */
	public static Multimap<String, String> ontoExpression;
	
	public static Multimap<String, Integer> IntentionExpression;
	
	/**  权重的map  本体类的id 为key  权重为value  **/ 
	public static Map<String,Integer> ontoWeight;
	
	
	/**
	 * 本体类默认的 子分类
	 */
	public static Map<String,String> ontoDefalutCate;
	
	
	public static Simple simple;
	
	public static Multimap<String,String> ontolongyElementMap;
	
	
	public static String realPath="";

    public static MaxWord maxWord;
  
    /**
     * 表达式里面的关键词   模式 表达式  key_表达式中的 词，value_表达式。 例如：发票=e_发票+c_投诉
     */
    public static Multimap<String,String> ontoExpMap;


    public static Logger logger;
    
    public static  String filterWord;
    
    /**
     * 所有的表达式里面的关键信息索引
     */
    public static Multimap<String,String> onElcepMap; 
    
    
    /**
     * 	生成 本体的树的关系  超级父类的id: 0_0
     * 	0_0  -----
     * 			  -----1_0
     * 					  -----1_10
     * 			  -----2_0
     * 			  		
     * 
     */
    public static MultiBTree  ontoLevelTree;
    
    
    /**
     * 		每个类id 和它资源的对应关系, 如 {1_10={k_中国，e_美国+c_中国}   }
     */
    public static Multimap<String,String> ontoResource; 
    
    
    public static Map<String,String> ontoInnerCodes;

	public static Map<String,String> ontoName;
	
	
	public static MultiKeyMap ontoResFactors;
	
	public static Map<String,Integer>  pfilters;
    
}
