package com.dinfo.oec.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dinfo.init.ControlConfig;
import com.dinfo.oec.bean.ExpressionInfo;
import com.dinfo.oec.bean.OntologyInfo;
import com.dinfo.oec.bean.ParseConfig;
import com.dinfo.oec.bean.ParseInfo;
import com.dinfo.oec.bean.StatusMsg;
import com.dinfo.oec.db.ParseDb;
import com.dinfo.oec.util.CommonUtil;
import com.dinfo.oec.util.RegexExpression;
import com.dinfo.oec.util.Utils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class AnalyClassify {
    private static final Logger log = Logger.getLogger(AnalyClassify.class);
	private ParseDb db = CommonUtil.getParseDb();
	
	/**
	 * 获取最适合的结果集
	 * @param cates		
	 * @param concepts
	 * @param elements
	 * @return
	 */
	public List<String> getBestOntology(List<String> cates,
			ArrayList<String> concepts, ArrayList<String> elements) {

		Set<String> catlogs = null;
		List<String> topcate = new ArrayList<String>();
		Multimap<Integer, String> topmap =ArrayListMultimap.create();
		for(String cate :cates)
		{
			if(cate.length()<5)
			{
				if(ParseConfig.ontoWeight.containsKey(cate)){
					topmap.put(ParseConfig.ontoWeight.get(cate), cate);
					topcate.add(cate);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(topcate))
		{	
			int maxcat = Collections.min(topmap.keySet());
			topcate =  (List<String>)topmap.get(maxcat);
			int topcatesize = topcate.size();
			int step=1;
			for(int i = 0 ;i<topcatesize && i>-1&topcatesize>0 ; i=i+step)
			{
				String cate = topcate.get(i);
				boolean flag = false;
				for(String subcate :cates)
				{
					if(subcate.contains(cate)&&subcate.length()>4){
						topcate.add(subcate);
						flag = true;
					}
				}
				if(!flag)
				{
					cate = ParseConfig.ontoDefalutCate.containsKey(cate)? ParseConfig.ontoDefalutCate.get(cate) :cate;
					topcate.set(i, cate);
					step = 1;
				}else{
					topcate.remove(i);
					step = 0;
					topcatesize--;
				}
			}
			catlogs = new HashSet<String>(topcate);
		}else{
			catlogs = new HashSet<String>(cates);
		}
		return sortByParent(new ArrayList<String>(catlogs));

	}

	public static List<String> sortByParent(List<String> cates)
	{
		List<String> result = new ArrayList<String>();
		Map<String,Integer> countmap  =  new HashMap<String, Integer>();
		Multimap<String, String>  map = ArrayListMultimap.create();
		for(String key :cates)
		{	
			int weight = ParseConfig.ontoWeight.containsKey(key)?ParseConfig.ontoWeight.get(key):65535;
			countmap.put(key, weight);
		}
		List<String> sortkeys = Utils.sortMapKey(countmap);
		return sortkeys;
	}

//	public ParseInfo classOntologyByExp(Map<Integer, String> classresult,
//			List<String> segs, ArrayList<String> concepts, ArrayList<String> elements,String exp) {
//		
//		ParseInfo info = new ParseInfo();
//		RegexExpression expression = new RegexExpression();
//		if (CollectionUtils.isNotEmpty(segs)) {
//			synchronized (AnalyClassify.class) {
//				expression.msg = new StatusMsg();
//				boolean status = expression.regexExpressionByCatelog(exp,
//						classresult, new HashSet<String>(segs));
//				Utils.addCollection(info.getCatlogs(),ParseConfig.ontoExpression.get(exp));
//				List<String> msgList = expression.msg.getMatchInfos();
//				Utils.addCollection(info.getMatchInfos(),msgList);
//				info.setStatus(status);
//				expression.msg = null;
//			}
//		}
//		if (CollectionUtils.isNotEmpty(info.getCatlogs())) {
//			if(ControlConfig.isEnabBestOntology()){	
//				info.setCatlogs(getBestOntology(info.getCatlogs(),concepts ,elements));
//			}else{	
//				info.setCatlogs(distinct(info.getCatlogs()));
//			}
//		}
//
//		return info;
//	}

	/**
	 * 
	 * @param segs
	 * @param content			
	 * @param conceptValues		分词结果
	 * @return 					概念结果{1_2001,2_3001}
	 */
	public ArrayList<String> classConcept(List<String> segs, String content,TreeMap<String, ArrayList<String>>  conceptValues) {
		if(null==content)
			return null;
		ArrayList<String> cates = new ArrayList<String>();
		List<String> keycate = classConceptKey(segs,conceptValues);
		List<String> concate = classConceptReg(content, segs);
		// Utils.addCollection(cates, keycate);
		if (CollectionUtils.isNotEmpty(keycate))
			cates.addAll(keycate);
		if (CollectionUtils.isNotEmpty(concate))
			cates.addAll(concate);
		//return dislodgeRepeat(cates);
		return cates;
	}
	
	private ArrayList<String> dislodgeRepeat(ArrayList<String> olist){
		ArrayList<String> list = new ArrayList<String>();
		for(String str:olist){
			if(!list.contains(str)){
				list.add(str);
			}
		}
		return list;
	}
	

	/**
	 * 
	 * @param content
	 * @return
	 */
	public List<String> classConceptKey(List<String> segs,TreeMap<String, ArrayList<String>>  conceptValues) {
		List<String> cates = new ArrayList<String>();
		int i=0;
		for (String seg : segs) {
			if (ParseConfig.conceptKeyRes.containsKey(seg)) {
				Collection<String> colls = ParseConfig.conceptKeyRes.get(seg);
				for(int j=0;j<colls.size();j++){
					conceptValues.put(i+"_"+seg,new ArrayList<String>(colls));
					}
				Utils.addCollection(cates, colls);
			}
			i++;
		}
		return cates;
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public List<String> classConceptReg(String content, List<String> segs) {
		List<String> cates = new ArrayList<String>();
		Multimap<String, String> map = CommonUtil.optimizatReg(
				ParseConfig.conceptRegRes, segs, content); // 优化正则表达式的查询范围
		if (null != map)
			for (String reg : map.keySet()) {
				if (CommonUtil.isMatch(content, reg)) {
					Utils.addCollection(cates, ParseConfig.conceptRegRes
							.get(reg));
					ParseConfig.logger.debug("匹配到的概念正则表达式是："+reg);
				}
			}
		return cates;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param segs			分词结果
	 * @param content		内容
	 * @param elementValues	 存储分词结果的容器
	 * @return		返回匹配到的要素的id  {1_100,1_1001,2_2001}
	 */
	public ArrayList<String> classElement(List<String> segs, String content,TreeMap<String, ArrayList<String>>  elementValues) {
		if(null==content)
			return null;
		ArrayList<String> cates = new ArrayList<String>();
		//文本信息
		List<String> keycate = classElementKey(segs,elementValues);
		//正则信息
		List<String> concate = classElementReg(content, segs,elementValues);
		if (CollectionUtils.isNotEmpty(keycate))
			Utils.addCollection(cates, keycate);
		if (CollectionUtils.isNotEmpty(concate))
			Utils.addCollection(cates, concate);
		return cates;
	}

	public List<String> classElementKey(List<String> segs,TreeMap<String, ArrayList<String>>  elementValues) {
		if(null==segs)
			return null;
		List<String> cates = new ArrayList<String>();
		int i=0;
		for (String seg : segs) {
			if (ParseConfig.elementKeyRes.containsKey(seg)) {
				//添加元素值。key.toArray()
				Collection<String> key = ParseConfig.elementKeyRes.get(seg);
				for(int j=0;j<key.size();j++){
				elementValues.put((i++)+"_"+seg,Lists.newArrayList(key));
				}
				Utils.addCollection(cates, key);
				i++;
			}
		}
		return cates;
	}

	public List<String> classElementReg(String content, List<String> segs, TreeMap<String, ArrayList<String>>  elementValues) {
		List<String> cates = new ArrayList<String>();
		Multimap<String, String> map = CommonUtil.optimizatReg(
				ParseConfig.elementRegRes, segs, content); // 优化正则表达式的查询范围
		if (null != map)
			for (String reg : map.keySet()) {
				if (CommonUtil.isMatch(content, reg)) {
					String res = CommonUtil.matchStr(content, reg);
					Utils.addCollection(cates, ParseConfig.elementRegRes
							.get(reg));
					ParseConfig.logger.debug("匹配到的要素正则表达式是："+reg);
					elementValues.put(res, Lists.newArrayList(map.get(reg)));
				}
			}
		return cates;
	}

	
	/***
	 * 这段代码有用暂时不用，误删！
	 * 
	 * //					for(String ontoid :ontoids)
//					{
//						if(CommonUtil.ontologyInfoContainkey(ontologyInfos, ontoid))
//						{
//							for(OntologyInfo ontoinfo :ontologyInfos)
//							{
//								if(ontoinfo.equals(ontoid))
//								{
//									if(!ontoinfo.getExpressionInfos().contains(expressionInfo))
//									{
//										ontoinfo.getExpressionInfos().add(expressionInfo);
//										double maxwright =CommonUtil.getMaxweight(ontoinfo.getExpressionInfos());
//										ontoinfo.setWeight(maxwright);
//									}
//								}
//							}
//						}else{
//							OntologyInfo ontologyInfo  =new OntologyInfo();
//							ontologyInfo.getExpressionInfos().add(expressionInfo);
//							ontologyInfo.setOntoid(ontoid);
//							if(ParseConfig.ontologyMap.containsValue(ontoid))
//								ontologyInfo.setOntoname(ParseConfig.ontologyMap.getKey(ontoid).toString());
//							ontologyInfo.setOntopath(ParseConfig.ontologyPath.get(ontoid));
//							double maxwright =CommonUtil.getMaxweight(ontologyInfo.getExpressionInfos());
//							ontologyInfo.setWeight(maxwright);
//							ontologyInfos.add(ontologyInfo);
//						}
//					}
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	

	/**
	 * 
	 * @param classresult   分类的  {1={c_概念信息+e_要素信息+分句1}，2={c_概念信息+e_要素信息+分句2}，
	 * 						-1={c_概念信息+e_要素信息+分句}}		
	 * @param segs			分词结果
	 * @param concepts		概念结果
	 * @param elements		要素结果
	 * @param pcate_id		父节点的id
	 * @param level			层次
	 * @return
	 */
	public ParseInfo classOntology(Map<Integer, String> classresult,
			List<String> segs, ArrayList<String> concepts, ArrayList<String> elements,
			String pcate_id, int level
	){
		ParseInfo info = new ParseInfo();
		List<OntologyInfo> ontologyInfos = new ArrayList<OntologyInfo>();
		List<String> expList = CommonUtil.optimizatOntologyByMap(concepts, elements,segs,pcate_id,level);
		List<String>  childrens = null ;
		if(StringUtils.isNotBlank(pcate_id))
			childrens=ParseConfig.ontoLevelTree.getChildStrByLevel(pcate_id, level);
		RegexExpression expression = new RegexExpression();
		log.info("匹配到的元素："+ControlConfig.local.get()+":"+elements);
		log.info("匹配到的概念："+ControlConfig.local.get()+":"+concepts);
		log.info("分词结果："+ControlConfig.local.get()+":"+segs);
		log.info("符合匹配条件的表达式："+ControlConfig.local.get()+":"+expList);
		//注意一定要删除。
		ControlConfig.local.remove();
		if (CollectionUtils.isNotEmpty(segs)) {
			for (String exp : expList) {
				expression.msg = new StatusMsg();
				
				boolean status = expression.regexExpressionByCatelog(exp,
						classresult, new HashSet<String>(segs));
				if (status) {
					double weight =countweight(expression,concepts,elements,segs);
					
					List<String> ontoids = (List<String>)ParseConfig.ontoExpression.get(exp);
					info.getExpressions().add(exp);
					for(String  onto :ontoids)
					{
						double factor=0;
						if(ParseConfig.ontoResFactors.containsKey(onto, exp))
							factor =Double.parseDouble(ParseConfig.ontoResFactors.get(onto, exp).toString());;
						if(info.getCatlogs().contains(onto))
						{
							int pos = info.getCatlogs().indexOf(onto);
							if(weight>info.getWeights().get(pos))
								info.getWeights().set(pos, weight);
							if(factor>info.getFactors().get(pos))
								info.getFactors().set(pos, factor);
						}else{
							if(null==childrens||childrens.contains(onto))
							{	
								info.getCatlogs().add(onto);
								info.getWeights().add(weight);
								info.getFactors().add(factor);
								info.getCatlognames().add(ParseConfig.ontoName.get(onto).toString());
								Utils.addCollection(info.getMatchInfos(),expression.msg.getMatchInfos());
								
							}
						}
					}
				}
				expression.msg = null;
			}

	}

//		if(ControlConfig.isEnabBestOntology()){	
//			info.setCatlogs(getBestOntology(info.getCatlogs(),concepts ,elements));
//		}else{	
//			info.setCatlogs(distinct(info.getCatlogs()));
//		}
//		info.setOntologyInfos(ontologyInfos);
		
		return info;
	}
	
//	private static List<String> distinct(List<String> list){
//
//		Set<String> set = new HashSet<String>(list);
//		List<String> newList = Lists.newArrayList(set);
//		return newList; 
//	}

	/**
	 * 
	 *  a*sum(ECK)/{a*sum(ECK)+ofno(inECK)+ofNO(ExpECK)}
	 * ECK是表达式项，包括e_、c_、k_、o_Com(ECK)是输入内容和表达式匹配的表达式项个数，
	 * 如果某一项在输入里出现多次，这里也要计算多次表示o_的个数  OfNo（InECK）是输入内容中没有匹配的表达式项个数 
	 * OfNO（ExpECK）是表达式中没有匹配的表达式项个数
	 * @param segs  分词结果
	 * @param elements  要素结果
	 * @param concepts  概念结果
	 * @param exp 		表达式
	 * 	
	 */
	private double countweight(RegexExpression exp, List<String> concepts, List<String> elements, List<String> segs
			)
	{	
		double weight=0;
		int totalconceptcount=concepts==null?0:concepts.size();
		int totalelementcount=concepts==null?0:elements.size();
		int totalkeycount=concepts==null?0:segs.size();
		int conceptcount =exp.msg.getConcepts()==null?0:exp.msg.getConcepts().size();
		int elementcount =exp.msg.getConcepts()==null?0:exp.msg.getElements().size();
		int ontocount=exp.msg.getOntology()==null?0:exp.msg.getOntology().size();
		int keycount =exp.msg.getMatchwords()==null?0:exp.msg.getMatchwords().size();
		
		int matchcount =(conceptcount+elementcount+keycount);
		int eck  =((ontocount+1)*matchcount);
		int ineck = (totalconceptcount+totalelementcount+totalkeycount)-(matchcount);
		int expeck= exp.msg.expresscount- matchcount;
		weight = eck*1.0/((eck+ineck+expeck)==0 ? 1:(eck+ineck+expeck));
//		double factor = Double.parseDouble(ParseConfig.ontoResFactors.get(onto, expstr).toString());
//		weight = weight*(Math.pow(10, factor));
		weight = (new BigDecimal(weight)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		return weight;
	}
	


	public List<String> getOntoPathByOntologyids(Collection<String> cates) {
		if (CollectionUtils.isEmpty(cates))
			return null;
		List<String> names = new ArrayList<String>();
		for (String cate : cates) {
			names.add(ParseConfig.ontologyPath.get(cate));
		}
		ParseConfig.logger.debug("匹配到的路径是："+names);
		return names;
	}



//	public Integer intentionParse(String catlog,Map<Integer, String> classresult,
//			List<String> segs, ArrayList<String> concepts, ArrayList<String> elements) {
//		Multimap<String, String>  optimizatIntention = getOptimizatIntention(catlog);
//		ParseInfo info = new ParseInfo();
//		//ParseConfig.logger.debug("过滤之前的"+ParseConfig.ontoExpression.toString());
//		Multimap<String, String> expMap = CommonUtil.optimizatIntention(optimizatIntention,concepts, elements,segs);
//		//ParseConfig.logger.debug("过滤之后的"+expMap.toString());
//		RegexExpression expression = new RegexExpression();
//		if (CollectionUtils.isNotEmpty(segs)) {
//			for (String exp : expMap.keySet()) {
//				expression.msg = new StatusMsg();
//				boolean status = expression.intentionRegexExpressionByCatelog(exp,
//						classresult, new HashSet<String>(segs));
//				if (status) {
//					Utils.addCollection(info.getCatlogs(),expMap.get(exp));
//					Utils.addCollection(info.getMatchInfos(),expression.msg.getMatchInfos());
//				}
//				expression.msg = null;
//			}
//	}
//		if (CollectionUtils.isNotEmpty(info.getCatlogs())) {
//			info.setCatlogs(distinct(info.getCatlogs())) ;
//		}
//		System.out.println(ArrayUtils.toString(">>"+info.getCatlogs()));
//		Set<String> matchInfos = info.getMatchInfos();
//		Integer result = getIntentionByCatlog(matchInfos);
//		return result;
//	}

	private Integer getIntentionByCatlog(Set<String> matchinfoSet) {
		// TODO Auto-generated method stub
		Iterator<String> it = matchinfoSet.iterator();
		List<String[]> list = Lists.newArrayList();
		while(it.hasNext()){
			String str = it.next();
			String id = str.substring(str.indexOf(": "),str.lastIndexOf(": ")).replace(": ", "");
			if(id.equals("")){
				return 0;
			}
			id = id.substring(0,id.lastIndexOf("_"));
			String exp = str.substring(str.lastIndexOf(": ")).replace(": ", "");
			System.out.println(id);
			System.out.println(exp);
			String[] keyValue = new String[]{id,exp};
			list.add(keyValue);
		}
		List<Integer> result = db.getintentionValue(list);
		System.out.println(ArrayUtils.toString(result));
		int  score = 0;
		for(Integer s:result){
			score +=s;
		}
		return score;
	}

	private Multimap<String, String> getOptimizatIntention(String catlog) {
		// TODO Auto-generated method stub
		catlog = catlog.substring(catlog.indexOf("_")+1);
		return db.getAllOntoIntentionExp(catlog);
	}
}
