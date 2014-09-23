package com.dinfo.oec.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import com.dinfo.init.DbConfig;
import com.dinfo.oec.bean.ConceptValue;
import com.dinfo.oec.bean.ElementValue;
import com.dinfo.oec.bean.ExpressionInfo;
import com.dinfo.oec.bean.JobGroup;
import com.dinfo.oec.bean.JobInfo;
import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.OntologyInfo;
import com.dinfo.oec.bean.ParseConfig;
import com.dinfo.oec.bean.ParseInfo;
import com.dinfo.oec.db.ParseDb;
import com.dinfo.oec.db.ParseMySql;
import com.dinfo.oec.db.ParseOracle;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class CommonUtil {
	
	
	public static   List<JobGroup> groupJob(List<JobItem> items)
	{
	   Multimap<String, JobItem> myMultimap = ArrayListMultimap.create();
	   List<JobGroup> list = Lists.newArrayList();
	   for(JobItem jobItem:items){
		   myMultimap.put(jobItem.getJobid(), jobItem);
	   }
	   Set<String> keySet = myMultimap.keySet();
       for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
    	   JobGroup jobGroup = new JobGroup();
    	   String key = (String) it.next();
    	   jobGroup.setJobid(key);
    	   jobGroup.setItems((List<JobItem>)myMultimap.get(key));
    	   list.add(jobGroup);
       }
	   return list;
	}
	
	
	public static List<String> segContent(String content)
	{
		if(null==content)
			return null;
		String[] seg = ParseConfig.simple.cutWord(content);
		return new ArrayList<String>(Arrays.asList(seg));
	}
	/**
	 * 处理权重
	 * @param info
	 */
	public static void countWeight(JobInfo info){
	    List<JobItem> jobList = info.getJobList();
	    for(int i =0;i<jobList.size();i++){
	        weightItem(jobList.get(i));
	    }
	}
	
	public static String matchStr(String content , String regex)
	{	
		String res = "";
		if(StringUtils.isBlank(regex)||StringUtils.isBlank(content))
			return res;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			int s =matcher.toMatchResult().start();
			int e = matcher.toMatchResult().end();
			res = content.substring(s, e);
		}
		return res;
	}
	
	
	private static void weightItem(JobItem item) {
        // TODO Auto-generated method stub
	    List<String> catlogPath = item.getCatlogPath();
	    if(CollectionUtils.isEmpty(catlogPath))
	    	return ;
	    if(catlogPath.size() == 1){
	        item.getWeights().add("0.00");
	        item.getWeights().set(0,0.9+"");
	    }else if(catlogPath.size()>1){
	        ParseInfo pInfo = item.getParseInfo();
	        int eCount =CollectionUtils.isNotEmpty(pInfo.getElements())? pInfo.getElements().size():0;
	        if(eCount == 0){
	            eCount =CollectionUtils.isNotEmpty(pInfo.getConcepts())? pInfo.getConcepts().size():0;
	        }
	        int sum = pInfo.getCatlogs().size();
	        for(int i =0;i<sum;i++){
	            item.getWeights().add("0.00");
	            eCount = eCount+eCount/5;
	            double weight = devide(i,sum,eCount,item.getWeights());
	            item.getWeights().set(i,weight+"");
	        }
	        
	    }
        
    }
	

   /* private static Multimap<String, String> getMatchWord(Set<String> match) {
        // TODO Auto-generated method stub
        Multimap<String, String> map = HashMultimap.create();
        for(String sent:match){
            sent = sent.replace("本体元素是:","").trim();
            if(sent.startsWith(":")){
                continue;
            }
            String obj = sent.split(":")[0].trim();
            String exp = sent.split(":")[1].trim();
            obj = obj.replaceAll("[1-9](_[0-9]*)*_?","").trim();
            Pattern p = Pattern.compile("[\u4E00-\u9FFFa-z1-9]{1,50}\\s*(\\(匹配\\)){1}");
            Matcher m = p.matcher(exp);
            while(m.find()){
                String w = m.group().replaceAll("\\(匹配\\)","").trim();
                map.put(obj,w);
            }
        }
        return map;
    }*/


    private static double devide(int index, int sum, int eCount, List<String> weight) {
        // TODO Auto-generated method stub
        //前一个元素的 权重
        BigDecimal last = new BigDecimal("0.00");
        BigDecimal res = new BigDecimal("2.00");
        if(index != 0){
            last  = new BigDecimal(weight.get(index-1));
        }
        //计算 当前 权重
        BigDecimal s = BigDecimal.valueOf(sum);
        BigDecimal e = BigDecimal.valueOf(eCount);
        try{
            res =  s.divide(e,2, BigDecimal.ROUND_HALF_EVEN);  
        }catch(Exception ex){}
       
        
        //处理 当前 权重， 分别 和 1，0比较   和 前一个 元素 比较，
        if(index == 0){
            if((res.compareTo(new BigDecimal("1")) == 1) || eCount == 0){
                res = new BigDecimal("0.9");
            }else if(res.compareTo(new BigDecimal("0.5")) == -1){
                res = new BigDecimal("0.5");
            }
        }else{
            if(res.compareTo(new BigDecimal("1")) == 1){
                res = last.subtract(new BigDecimal("0.05"));
            }
            if(res.compareTo(last) == 1){
                res = last.subtract(new BigDecimal("0.05"));
            }
            if(res.compareTo(new BigDecimal("0")) == -1){
                res = last;
            }
        }
        return res.doubleValue();
    }


    public static Multimap<String, String> optimizatReg(Multimap<String, String> res ,List<String> seg,String content)
	{  
	    if(CollectionUtils.isEmpty(seg)){
	        return null;
	    }
	    content = content.toLowerCase();
	    Set<String> set = null;
	    Multimap<String,String> resMap = ArrayListMultimap.create();
	    for(String model:res.keySet()){
	        set = new HashSet<String>(Arrays.asList(model.split("[|+{}*$\\[\\]$^?,\\-()\\\\&./_]")));
	        set.remove("");
	        if(set.size() == 1){
	            if(content.indexOf(model.toLowerCase()) != -1){
	                resMap.putAll(model,res.get(model));
	            }
	            continue;
	        }
	        int num = 0;
	        for(String str:set){
//	            str = str.toLowerCase();
//	            if(str.matches("[0-9]*") || str.matches("[A-Za-z]")){
//	                continue;
//	            }

	            if(content.indexOf(str) != -1){
	                num++;
	            }
	            if(num == 2){
	                resMap.putAll(model,res.get(model));
                    break;
                }
	        }
	    }
		return resMap;
	}
    
	
	/**
	 * 
	 * @param concepts  分析到的概念  但是是概念的id, 1_001
	 * @param elements	分析到的要是，但是也是id  1_001_004
	 * @param segs		对应的分词结果  [中国，美国]
	 * @return
	 */
	public static  Multimap<String, String>  optimizatOntology(ArrayList<String> concepts , ArrayList<String> elements,List<String> segs)
	{
		Set<String> set = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(concepts)){
		    for(String key:concepts){
		        set.add((String) ParseConfig.conceptMap.getKey(key));
		    }
		}
		if(CollectionUtils.isNotEmpty(elements)){
		    for(String key:elements){
		        if(key.equals("否定")){
		            continue;
		        }
		        set.add((String) ParseConfig.elementMap.getKey(key));
		    }
		}
		if(CollectionUtils.isNotEmpty(segs)){
		    set.addAll(segs);
		}
		Multimap<String, String>  resMap = ArrayListMultimap.create();
		Set<String> expSet = new HashSet<String>();
		for(String word:set){
		   if(ParseConfig.ontoExpMap.containsKey(word)){
		       expSet.addAll(ParseConfig.ontoExpMap.get(word));
		   }
		}
		for(String exp:expSet){
		    resMap.putAll(exp,ParseConfig.ontoExpression.get(exp));
		}
		return resMap;
	}
	
	/**
	 * 
	 * @param concepts
	 * @param elements
	 * @param segs
	 * @param pcate_id
	 * @param level
	 * @return
	 */
	public static  List<String> optimizatOntologyByMap(ArrayList<String> concepts , ArrayList<String> elements,List<String> segs,
			String pcate_id,int level)
	{	
		List<String> result = new ArrayList<String>();
		List<String> templist = new ArrayList<String>();
		List<String> findKey =new ArrayList<String>();
		Utils.addCollection(findKey, Utils.concatListWithPrefix(toElementName(elements), "e_"));
		Utils.addCollection(findKey, Utils.concatListWithPrefix(toConceptName(concepts), "c_"));
		Utils.addCollection(findKey,Utils.concatListWithPrefix(segs, "k_"));
		for(String key :findKey)
			Utils.addCollection(templist, ParseConfig.onElcepMap.get(key));
		if(StringUtils.isBlank(pcate_id))
		{
			result = templist;
		}else{
			List<String> childs = ParseConfig.ontoLevelTree.getChildStrByLevel(pcate_id, level);
			for(String key :childs)
			{
				Utils.addCollection(result,ParseConfig.ontoResource.get(key));
			}
			if(CollectionUtils.isNotEmpty(templist))
				result.retainAll(templist);
			
		}
		return result;
	}
	
	/**
	 * 查找是否匹配
	 * @param content 	内容		
	 * @param regex		正则表达式	
	 * @return
	 */
	public static boolean  isMatch(String content , String regex)
	{	
		if(StringUtils.isBlank(regex)||StringUtils.isBlank(content))
			return false;
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(content);
			return matcher.find();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ParseConfig.logger.debug("错误的正则表达式是："+regex);
		}
		return false;
	}

	
	public  static String toCateResult(ArrayList<String> conceptcates , ArrayList<String> elementcates , String  content){
		List<String> list = new ArrayList<String>();
		String result ="";
		String conceptstr = Utils.concatWithPrefix(conceptcates, "c");
		if(StringUtils.isNotBlank(conceptstr))
			list.add(conceptstr);
		String elementstr = Utils.concatWithPrefix(elementcates, "e");
		if(StringUtils.isNotBlank(elementstr))
			list.add(elementstr);
		result = Utils.concat(list);
		result = result+"  "+content;
		return result;
		
	}
	
	public static String figureSpeed(long start, long end, List<JobItem> items) {
		DecimalFormat format = new DecimalFormat("####.##");
		String speed="";
		end = start==end ? start+1 :end;
		float intval = (float)(end-start);
		double total = 0;
		double  sp = 0;
		for(JobItem job:items){
			if(job.getContent()!=null){
			total += job.toString().getBytes().length;
			}
		}
		sp = total/intval;
		if(sp>1000000){
			speed = format.format(sp/1000000)+"M/ms";
		}else if(sp>1000){
			speed = format.format(sp/1000)+"kb/ms";
		}else{
			speed =format.format(sp)+"b/ms";
		}
		return speed;
	}
	
	
	/**
	 * 元素值返回的匹配结果map
	 */
	public static Map<String,ArrayList<String>> returnElementValueMap(Map<String, ArrayList<String>> elementValues)
	{
		Multimap<String, String> mulmap = ArrayListMultimap.create();
		Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		
		Iterator<String> cit = elementValues.keySet().iterator();
		String ckey="";
		Object keys = null;
		while(cit.hasNext()){
			ArrayList<String> tmpList = new ArrayList<String>();
			ckey = cit.next();
			tmpList = elementValues.get(ckey);
			if(CollectionUtils.isNotEmpty(tmpList)){
				keys = ParseConfig.elementMap.getKey(tmpList.get(0));
			}
			if(keys != null){
				mulmap.put(keys.toString(), ckey);
			}
		}
		if(mulmap.size()>0){
			Set<String> set  = mulmap.keySet();
			for(String key:set){
				Collection<String> c = mulmap.get(key);
				ArrayList<String> list = new ArrayList<String>();
				list.addAll(c);
				map.put(key, list);
			}
		}
		
		return map;
	}
	
	
	/**
	 * 概念值返回的匹配结果map
	 * @param conceptValues
	 * @return
	 */
	public static  Map<String,ArrayList<String>>  returnConceptValueMap(Map<String, ArrayList<String>> conceptValues)
	{
		Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		Multimap<String, String> mulmap = ArrayListMultimap.create();
		Iterator<String> cit = conceptValues.keySet().iterator();
		String ckey="";
		Object keys = null;
		while(cit.hasNext()){
			ArrayList<String> tmpList = new ArrayList<String>();
			ckey = cit.next();
			tmpList = conceptValues.get(ckey);
			if(CollectionUtils.isNotEmpty(tmpList)){
				keys = ParseConfig.conceptMap.getKey(tmpList.get(0));
			}
			if(keys != null){
				mulmap.put(keys.toString(), ckey);
			}
		}
		if(mulmap.size()>0){
			Set<String> set  = mulmap.keySet();
			for(String key:set){
				Collection<String> c = mulmap.get(key);
				ArrayList<String> list = new ArrayList<String>();
				list.addAll(c);
				map.put(key, list);
			}
		}
		
		return map;
	}
	
	
	
	public static String elapsed(long start, long end) {
		// TODO Auto-generated method stub
		String time  =DurationFormatUtils.formatPeriod(start, end, "mm 分 ss 秒  SS 毫秒");
		return time;
	}
	
	public static String  concatJobItems(List<JobItem> items)
	{
		String  result = "";
		StringBuilder sb  = new StringBuilder();
		for(JobItem item :items)
		{
			sb.append(item.getContent()+"。");
		}
		return sb.toString();
	}
	//
	public static ArrayList<String> toConceptName(Collection<String>  concepts)
	{
		ArrayList<String>   names = new ArrayList<String>();
		if(CollectionUtils.isEmpty(concepts))
		 {
			 return null;
		 }
		for(String   set : concepts)
		{
			if(ParseConfig.conceptMap.getKey(set)==null){
				System.out.println(set+">>>>>>>>>>>");
			}
			names.add(ParseConfig.conceptMap.getKey(set).toString());
		}
		return  names;
	}
	private static ArrayList<String> dislodgeRepeat(ArrayList<String> olist){
		ArrayList<String> list = new ArrayList<String>();
		for(String str:olist){
			if(!list.contains(str)){
				list.add(str);
			}
		}
		return list;
	}
	
	//
	public static ArrayList<String> toElementName(Collection<String>  elements)
	{
		ArrayList<String>   names = new ArrayList<String>();
		if(CollectionUtils.isEmpty(elements))
		 {
			 return null;
		 }
		for(String   set : elements)
		{	
			if(ParseConfig.elementMap.containsValue(set))
				names.add(ParseConfig.elementMap.getKey(set).toString());
		}
		return  names;
	}
	
	public static  String toFilterPattern(List<String> words)
	{
		return null;
	}
	
	public static String filterWord(String content)
	{
		if(StringUtils.isBlank(content))
			return null;
		List<String> words = new ArrayList<String>();
		String[] segs = content.split("(\\.|\\,|\\。|\\，)");
		for(String seg :segs)
		{
			if(!CommonUtil.isMatch(seg, ParseConfig.filterWord))
				words.add(seg);
		}
		content = Utils.concatWithEnd(words, ",");
		return content;
	}
	
	public static List<ElementValue> toElementValue(Map<String, ArrayList<String>> elementValues)
	{
		List<ElementValue> eleList = new ArrayList<ElementValue>();
		Iterator<String> eit = elementValues.keySet().iterator();
		String ekey="";
		while(eit.hasNext()){
			ElementValue elementValue = new ElementValue();
			ekey = eit.next();
			elementValue.setId(ekey);
			elementValue.setElementValues(elementValues.get(ekey));
			eleList.add(elementValue);
		}
		return eleList;
	}
	
	public static List<ConceptValue>  toConceptValue(Map<String, ArrayList<String>> conceptValues)
	{
		List<ConceptValue> conList = new ArrayList<ConceptValue>();
		Iterator<String> cit = conceptValues.keySet().iterator();
		String ckey="";
		while(cit.hasNext()){
			ConceptValue conceptValue = new ConceptValue();
			ckey = cit.next();
			conceptValue.setId(ckey);
			conceptValue.setConceptValues(conceptValues.get(ckey));
			conList.add(conceptValue);
		}
		
		return conList;
	}
	
	public static ParseDb  getParseDb()
	{
		if(DbConfig.dbtype.equals("mysql"))
		{
			return  (new ParseMySql());
		}else{
			return (new ParseOracle());
		}
	}
	
	public static boolean ontologyInfoContainkey(List<OntologyInfo>infos ,String key)
	{
		for(OntologyInfo info :infos)
		{
			if(info.getOntoid().equals(key))
				return true;
		}
		
		return false;
	}
	
	public static double  getMaxweight(List<ExpressionInfo> expressionInfos)
	{
		 double weight =0;
		 
		 for(ExpressionInfo info :expressionInfos)
		 {
			if(weight<info.getWeight())
				weight=info.getWeight();
		 }
		 return weight;
	}
	
	public static List<String> getParentsByStr(String cateid)
	{
		List<String> datas = new ArrayList<String>();
		String[] cates= cateid.split("_");
		String tree = cates[0];
		String pcate=tree+"_"+cates[1];
		datas.add(pcate);
		for(int i=2;i<cates.length;i++)
		{
			pcate+="_"+cates[i];
			datas.add(pcate);
		}
		return datas;
	}
	
	public static void innerCodeShared(ParseInfo info)
	{
		if(CollectionUtils.isNotEmpty(info.getCatlogs()))
		{
			List<String> innercodes = new ArrayList<String>();
			List<String> shares = new ArrayList<String>();
			
			for(String cate :info.getCatlogs())
			{
				String code="";
				String shared="";
				List<String> pcates = CommonUtil.getParentsByStr(cate);
				for(String pcate :pcates)
				{
					String innercode =ParseConfig.ontoInnerCodes.get(pcate);
					if(StringUtils.isNotBlank(innercode))
					{
						code+="->"+innercode.split("\\|")[0];
						shared+="->"+Integer.parseInt(innercode.split("\\|")[1]);
					}
				}
				
				if(StringUtils.isNotBlank(code))
				{
					code = code.substring(2);
					innercodes.add(code);
				}
					
				if(StringUtils.isNotBlank(shared))
				{
					shared = shared.substring(2);
					shares.add(shared);
				}
					
				
			}
			info.getInner_code().addAll(innercodes);
			info.getShared().addAll(shares);
			
		}
	}
	
	
	public static void sortOntology(ParseInfo info)
	{
		TreeMap<Integer,Double> poss = new TreeMap<Integer,Double>();
		
		List<String> catlogs = new ArrayList<String>();
		List<Double> weights = new ArrayList<Double>();
		List<String> catlogPath = new ArrayList<String>();
		List<String> inner_code = new ArrayList<String>();
		List<String> catlognames = new ArrayList<String>();
		List<String> shared = new ArrayList<String>();

		if(CollectionUtils.isEmpty(info.getCatlogs()))
			return;
		for(int i=0;i<info.getCatlogs().size();i++)
		{
			poss.put(i, info.getWeights().get(i));
		}
		List<Integer> sorts = Utils.sortMapKey(poss);
		
		for(Integer pos :sorts)
		{
			catlogs.add(info.getCatlogs().get(pos));
			weights.add(info.getWeights().get(pos));
			catlogPath.add(info.getCatlogPath().get(pos));
			inner_code.add(info.getInner_code().get(pos));
			shared.add(info.getShared().get(pos));
			catlognames.add(info.getCatlognames().get(pos));
		}
		info.getCatlogs().clear();
		info.getCatlogPath().clear();
		info.getWeights().clear();
		info.getInner_code().clear();
		info.getShared().clear();
		info.getCatlognames().clear();
		info.getCatlogs().addAll(catlogs);
		info.getCatlogPath().addAll(catlogPath);
		info.getWeights().addAll(weights);
		info.getInner_code().addAll(inner_code);
		info.getShared().addAll(shared);
		info.getCatlognames().addAll(catlognames);
		
	}
	
}
