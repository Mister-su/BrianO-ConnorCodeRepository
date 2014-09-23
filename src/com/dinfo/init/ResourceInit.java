package com.dinfo.init;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.chenlb.mmseg4j.example.Simple;
import com.dinfo.oec.bean.ParseConfig;
import com.dinfo.oec.db.ParseDb;
import com.dinfo.oec.util.CommonUtil;
import com.dinfo.oec.util.Utils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ResourceInit {
    private static final Logger log = Logger.getLogger(ResourceInit.class);
	
	private static ParseDb db = null;
	
	/**
	 *  初始化所有资源
	 */
	public static void init ()
	{	
		ParseConfig.logger = Logger.getLogger("debug");
//		ControlConfig.init();
		DataBaseConfig.init();
		db = CommonUtil.getParseDb();
		loadResource();
	}
	

	public static void loadResource(){
		
		/**  概念表的 关键词资源  对应关系 例如是  工单={1_10_01,2_1001}  **/
		ParseConfig.conceptKeyRes =db.getConceptResByType(0);
		/**  概念表的 正则表达式资源  对应关系 例如   发(*.)单={1_10_01,2_1001}  **/
		ParseConfig.conceptRegRes = db.getConceptResByType(1);
		/**  概念表的 关键词资源  对应关系 例如是  工单={1_10_01,2_1001}  **/
		ParseConfig.elementKeyRes = db.getElementResByType(0);
		/**  概念表的 正则表达式资源  对应关系 例如   发(*.)单={1_10_01,2_1001}  **/
		ParseConfig.elementRegRes = db.getElementResByType(1);
		ParseConfig.elementWeight = db.getElementWeight();
		
		ParseConfig.ontoName=db.getOntoNames();
		ParseConfig.ontologyMap =db.getOntologyMap();
		/**  本体对象对应的分类全路径  如 {1_1001_002 =订单类-->订单子类 } **/
		ParseConfig.ontologyPath= db.getOntologyPath();
		//所有表达式
		ParseConfig.ontoExpression = db.getAllOntoExpresssion();
		//所有正负面表达式。
		ParseConfig.IntentionExpression = db.getIntentionExp();
		//本体和元素关系表。
//		ParseConfig.ontolongyElementMap = db.geOntolongyElementMap();
		
		ParseConfig.ontoDefalutCate = db.getOntoDefaultCate();
		/** 概念名字和 概念id 的对应关系 该关系是双向的 **/
		ParseConfig.conceptMap = db.getAllConceptMap();
		/** 要素名字和 要素id 的对应关系 该关系是双向的 **/
		ParseConfig.elementMap = db.getAllElementMap();
		
		/** 这两个参数暂时不用 **/
		ParseConfig.ontoExpMap = db.getAllOntoExp();
		/**  权重的map  本体类的id 为key  权重为value  **/
		ParseConfig.ontoWeight = db.getOntoWeight();
		//ParseConfig.filterWord=db.getFilterWord();
		ParseConfig.ontoInnerCodes=db.getInnerCodes();
		ParseConfig.logger.debug("intercode is :"+ParseConfig.ontoInnerCodes.toString());
		
		ParseConfig.onElcepMap = indexRelation();
		
		ParseConfig.ontoLevelTree=db.getOntoLevelTree();
		
		ParseConfig.ontoResource=db.getAllOntoResource();
		
		/**  加权因子  **/
		ParseConfig.ontoResFactors =db.getAllOntoResFactors(); 
		
		ParseConfig.pfilters = db.getAllpfilters();
		
		initWordRes();
		ParseConfig.simple = new Simple();
		
	}
	
	/**
	 * 从表达式里面获取里面关键词 
	 * @return
	 */
	public static Set<String> readResourceFromExpress()
	{
		Set<String> resource  = new HashSet<String>();
		Set<String>  words = ParseConfig.ontoExpression.keySet();
		for(String key:words)
		{
			key =key.replaceAll("((c_[\u4E00-\u9FFFa-z1-9]{1,50})|(e_[\u4E00-\u9FFFa-z1-9]{1,50})|\\#|k|\\_)", "");
			String[] segs = key.split("(\\+|\\-|\\(|\\)|\\|)");
			if(ArrayUtils.isNotEmpty(segs))
			{	
				for(String  word :segs){
					if(StringUtils.isNotBlank(word))
						resource.add(word);
				}
			}
		}
		return resource;
	}
	
	/**
	 * 初始化分词字典
	 */
	public static void initWordRes() {
		Set set = new HashSet();
		Set rset = db.getAllResource();
		List<String> wordbases =db.getWordbase();
		List<String> worduse=db.getWorduserByType(0);
		List<String> wordfileter=db.getWorduserByType(1);
		Utils.addCollection(set, readResourceFromExpress());
		if (CollectionUtils.isNotEmpty(rset)) 
			set.addAll(rset);
		if(CollectionUtils.isNotEmpty(wordbases))
			set.addAll(wordbases);
		if(CollectionUtils.isNotEmpty(worduse))
			set.addAll(worduse);
		if(CollectionUtils.isNotEmpty(wordfileter))
			set.removeAll(wordfileter);
		try {
			File filedata = new File(ParseConfig.realPath+"data");
			if (!filedata.exists()) {
				filedata.mkdirs();
			}
			File file = new File(ParseConfig.realPath+"data/words-new.dic");
			log.debug("开始写入words-new.dic 词库文件");
			Iterator it = set.iterator();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			while (it.hasNext()) {
				String writekey = (String) it.next();
				pw.write(writekey + "\r\n");
				pw.flush();
			}
			log.debug("开始写入words-new.dic 词库文件完成");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("没有找到配置文件！",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("配置文件读取异常！",e);
		}
	}
	
	/**
	 * 对所有的概念和要素 建立对 本体资源的索引，如
	 * e_上门={#e_上门+k_安排#,#(e_上门+c_能够+c_到位)#,}
	 * 
	 * @return
	 */
	public static Multimap<String,String> indexRelation()
	{
		Set<String> set = makeExpressionSet();
		Multimap<String,String> map = ArrayListMultimap.create();
		//匹配到的概念或者要素存入map
		for(String con:set){			
			Set<String> cons = ParseConfig.ontoExpression.keySet();
			if(null != cons && cons.size()>0){
				for(String everyCon:cons){
					if(everyCon.contains(con)){
						String useCon = everyCon;
						map.put(con, useCon);
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 将所有表达式中的元素放入set集合
	 * @return
	 */
	public static Set<String> makeExpressionSet(){
		Set<String> eSet = new HashSet<String>();
		//将查出的表达式集合转为字符串,过滤掉无用标点符号
		String oeList = ParseConfig.ontoExpression.keySet().toString().replaceAll("(\\pS|\\-)",",");
		oeList = oeList.replaceAll("\\[|\\]|\\#|\\(|\\)|\\s", "");
		String[] oeArray = oeList.split("\\,");
		for(String str:oeArray){
			eSet.add(str);
		}
		return eSet;
	}
	
	
	public static void initconfig(String configpath,String datapath)
	{
		DataBaseConfig.initPropertis(configpath);
		System.setProperty("user.dir", datapath);
		if(!datapath.endsWith(File.separator))
			datapath=datapath+File.separator;
		ParseConfig.realPath=datapath;
	}
	
	public static void main(String[] args) {
		new ResourceInit().init();
		System.out.println(ParseConfig.onElcepMap);
	}
}









