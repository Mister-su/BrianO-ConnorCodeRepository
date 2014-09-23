package com.dinfo.oec.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.wltea.expression.ExpressionEvaluator;

import com.dinfo.oec.bean.ParseConfig;
import com.dinfo.oec.bean.StatusMsg;
import com.dinfo.oec.common.AnalyClassify;

public class RegexExpression {
    private static final Logger log = Logger.getLogger(AnalyClassify.class);

	public  StatusMsg msg ;


//	public boolean intentionRegexExpressionByCatelog(String regex, Map cates,Set<String> cutword ) {
//		if(regex.startsWith("@")&&regex.endsWith("@")){
//			regex = regex.replace("@", "");
//		}
//
//		String object ="";
//		if(msg.stackflag==false&&(ParseConfig.IntentionExpression.containsKey(regex)))
//		{
//			List<String> ontos  =(List<String>) ParseConfig.IntentionExpression.get(regex);
//			for(String onto:ontos)
//			{	
//				Object ontostr  = ParseConfig.ontologyMap.get(onto.trim());
//				if(null!=ontostr) { 
//					object += onto+"_" +ontostr.toString();
//				}else{
//					object += onto;
//				}
//			}
//		}
//		String infostr = "";
//		Map express = new HashMap<String, String>();
//		Map onlyexpress = new HashMap<String, String>();
//		regex =getExpression(regex, express);
//		regex = getOnlyExpression(regex, onlyexpress);
//		String pat = "(c_[\u4E00-\u9FFFa-z1-9]{1,50})|(k_[\u4E00-\u9FFFa-z1-9]{1,50})|(e_[\u4E00-\u9FFFa-z1-9]{1,50})|(o_[\u4E00-\u9FFFa-z1-9]{1,50})|(y_\\d)|(z_\\d)";  //切分以及 分析里面的类
//		List<String> list = new ArrayList<String>();
//		List<String> infolst = new ArrayList<String>();
//		Pattern pattern = Pattern.compile(pat);								   // 假如  k_中国+c_2;10+c_3;50 ， 分析 和棋切割成
//		Matcher matcher = pattern.matcher(regex);						 	   //[k_中国,c_2;10,c_3;50]	[,+,+]
//		while (matcher.find()) {											   
//			list.add(matcher.group(0));
//			infolst.add(matcher.group(0));
//		}
//		String[] seg = regex.split(pat);
//		String catelog = (String)cates.get(-1);
//		for (int i = 0; i < list.size(); i++) {									//拿类到结果集里面查询，查到把标志该字段标志位true
//			String it =list.get(i).substring(2);
//			if(list.get(i).startsWith("c_")){
//				Map<String, String> conceptMap = ParseConfig.conceptMap;
//				if(conceptMap.containsKey(it)){
//					if (catelog.indexOf("c_"+conceptMap.get(it)) != -1) {
//						list.set(i, "true");
//						infolst.set(i, "c_"+it);
//						if(StringUtils.isNotBlank(it))
//							msg.getConcepts().add(it);
//					} else {
//						list.set(i, "false");
//						infolst.set(i, "c_"+it);
//					}
//				}else{
//					list.set(i, "false");
//					infolst.set(i, "c_"+it);
//				}
//
//			}else if(list.get(i).startsWith("e_")){
//				Map<String, String> elementMap = ParseConfig.elementMap;
//				if(elementMap.containsKey(it)){
//					if (catelog.indexOf("e_"+elementMap.get(it)) != -1) {
//						list.set(i, "true");
//						infolst.set(i, "e_"+it);
//						if(StringUtils.isNotBlank(it))
//							msg.getElements().add(it);
//					} else {
//						list.set(i, "false");
//						infolst.set(i, "e_"+it);
//					}
//				}else{
//					list.set(i, "false");
//					infolst.set(i, "e_"+it);
//				}
//			}else if(list.get(i).startsWith("k_")){
//				if (catelog.indexOf(it) != -1) {
//					list.set(i, "true");
//					infolst.set(i, "k_"+it);
//				} else {
//					List<String> sameword = null;//WeiboResource.sameWord.get(it);			//处理同义词
//					if(Utils.isMatchSameWord(sameword, catelog)){
//						list.set(i, "true");
//						infolst.set(i, "k_"+it);
//					}else{
//						list.set(i, "false");
//						infolst.set(i, "k_"+it);
//					}
//				}
//			}else if(list.get(i).startsWith("o_")){
//				String subcon = null;//mdb.getConditionBysname(it);
//				Map map = new HashMap<String, String>();
//				map.putAll(cates);
//				if(intentionRegexExpressionByCatelog(subcon,map,cutword)){  
//					list.set(i, "true");
//					infolst.set(i, "o_"+it);
//				}else{
//					list.set(i, "false");
//					infolst.set(i, "o_"+it);
//				}
//			}else if(list.get(i).startsWith("y_")){
//				String  tmpexp = (String)express.get(list.get(i));
//				int size = cates.keySet().size();
//				Set<Integer> set = cates.keySet();
//				boolean flag = false ;
//				msg.stackflag =true;
//				for(Integer key :set){
//					if(-1!=key){
//						String value = (String)cates.get(key);
//						Map expmap = new HashMap();
//						expmap.put(-1, value);
//						if(intentionRegexExpressionByCatelog(tmpexp,expmap,cutword)){
//							flag=true;
//							break;
//						}
//					}
//				}
//
//				if(flag){
//					list.set(i, "true");
//					infolst.set(i, msg.stackstr);
//				}else{
//					list.set(i, "false");
//					infolst.set(i, "y_"+it);
//				}
//				msg.stackflag=false;
//				msg.stackstr="";
//			}else if(list.get(i).startsWith("z_")){
//				String  tmpexp = (String)onlyexpress.get(list.get(i));
//				int size = cates.keySet().size();
//				boolean flag = false;
//				if(size<3 && intentionRegexExpressionByCatelog(tmpexp,cates,cutword)){
//					String tmpstr = (String) cates.get(-1);
//					tmpstr = tmpstr.split("\\|")[1];
//					String reg = cutword.toString().replaceAll(",", "|");
//					reg = reg.replace("\\[|\\]", "").replaceAll(" ", "");
//					tmpstr = tmpstr.replaceAll(reg, "");
//					if(tmpstr.length()<5){
//						flag = true;
//					}
//				}
//				if(flag){
//					list.set(i, "true");
//					infolst.set(i, "z_"+it);
//				}else{
//					list.set(i, "false");
//					infolst.set(i, "z_"+it);
//				}
//			}
//
//
//		}
//		String tmp = "";
//		if(seg.length>0){
//			for (int i = 0; i < list.size(); i++) {
//				tmp += (seg[i] + list.get(i));//把结果和操作字符拼接起来
//				infostr += (seg[i] + infolst.get(i));
//			}
//			if(seg.length>list.size()){									
//				tmp += seg[list.size()];											//有括号的话的加上最后的括号
//				infostr+= seg[list.size()];
//			}
//		}else{
//			tmp =list.get(0);
//			infostr = infolst.get(0);
//		}
//		if(!msg.stackflag){
//			msg.getMatchInfos().add("本体元素是: "+object+": "+infostr);
//		}else{
//			msg.stackstr="#"+infostr+"#";
//		}
//		return expressionEvaluator(tmp);
//	}

	/**
	 * msg.stackflag 代表是不是再栈里面，false 代表不再栈，true代表在栈
	 * 
	 * 流程： 如果 表达式里面有# 或者@ 把该部分提取出来，存到临时的集合里面，然后替换为y 或者z .
	 * 然后把 表达式里面的项 和 运算符号分离
	 * 
	 * 场景模式
	 *  （c_开始+c_结束）+#c_将来-c_未来#
	 * @param regex  要匹配的表达式 
	 * @param cates 分析出来的类，以及该句话的详细信息
	 * @param cutword 分词的结果
	 * @return
	 */
	public boolean regexExpressionByCatelog(String regex, Map cates,Set<String> cutword ){
		List<String> infolst = new ArrayList<String>();
		String object ="";
		//拼接本地表达式
		if(msg.stackflag==false&&(ParseConfig.ontoExpression.containsKey(regex)))
		{
			List<String> ontos  =(List<String>) ParseConfig.ontoExpression.get(regex);
			for(String onto:ontos)
			{	
				Object ontostr  = ParseConfig.ontologyMap.get(onto);
				if(null!=ontostr){
					object += onto+"_" +ontostr.toString();
				}else{
					object += onto;
				}
			}
		}
		
//		boolean atFlag = regex.startsWith("@")&&regex.endsWith("@");
//		if(atFlag){
//			regex = regex.substring(1,regex.lastIndexOf('@'));
//		}
		
		
		String infostr = "";
		Map express = new HashMap<String, String>();
		Map onlyexpress = new HashMap<String, String>();
		regex =getExpression(regex, express);
		regex = getOnlyExpression(regex, onlyexpress);
		String pat = "(c_[\u4E00-\u9FFFa-z1-9]{1,50})|(k_[\u4E00-\u9FFFa-z1-9]{1,50})|(e_[\u4E00-\u9FFFa-z1-9]{1,50})|(o_[\u4E00-\u9FFFa-z1-9]{1,50})|(y_\\d)|(z_\\d)";  //切分以及 分析里面的类
		List<String> list = new ArrayList<String>();
		List<String> itList = new ArrayList<String>();
		Pattern pattern = Pattern.compile(pat);								   // 假如  k_中国+c_2;10+c_3;50 ， 分析 和棋切割成
		Matcher matcher = pattern.matcher(regex);						 	   //[k_中国,c_2;10,c_3;50]	[,+,+]
		while (matcher.find()) {											   
			list.add(matcher.group(0));
			itList.add(matcher.group(0));
			infolst.add(matcher.group(0));
		}
		
		if(msg.stackflag)
			msg.expresscount=list==null?0:list.size();
		
		String[] seg = regex.split(pat);
		String catelog = (String)cates.get(-1);
		
		for (int i = 0; i < list.size(); i++) {									//拿类到结果集里面查询，查到把标志该字段标志位true
			String it =list.get(i).substring(2);
			if(list.get(i).startsWith("c_")){
				Map<String, String> conceptMap = ParseConfig.conceptMap;
				if(conceptMap.containsKey(it)){
					if (catelog.indexOf("c_"+conceptMap.get(it)) != -1) {
						list.set(i, "true");
						infolst.set(i, "c_"+it+"(匹配)");
						if(StringUtils.isNotBlank(it)&&!msg.stackflag)
							msg.getConcepts().add(it);
					} else {
						list.set(i, "false");
						infolst.set(i, "c_"+it+"(不匹配)");
					}
				}else{
					list.set(i, "false");
					infolst.set(i, "c_"+it+"(不匹配)");
				}

			}else if(list.get(i).startsWith("e_")){
				Map<String, String> elementMap = ParseConfig.elementMap;
				if(elementMap.containsKey(it)){
					if (catelog.indexOf("e_"+elementMap.get(it)) != -1) {
						list.set(i, "true");
						infolst.set(i,"e_"+ it+"(匹配)");
						if(StringUtils.isNotBlank(it)&&!msg.stackflag)
							msg.getElements().add(it);
					} else {
						list.set(i, "false");
						infolst.set(i,"e_"+  it+"(不匹配)");
					}
				}else{
					list.set(i, "false");
					infolst.set(i, "e_"+ it+"(不匹配)");
				}
			}else if(list.get(i).startsWith("k_")){
				if (catelog.indexOf(it) != -1) {
					list.set(i, "true");
					infolst.set(i,"k_"+ it+"(匹配)");
					if(StringUtils.isNotBlank(it)&&!msg.stackflag)
						msg.getMatchwords().add(it);
				} else {
					List<String> sameword = null;		//处理同义词
					if(Utils.isMatchSameWord(sameword, catelog)){
						list.set(i, "true");
						infolst.set(i, "k_"+ it+"(匹配)");
					}else{
						list.set(i, "false");
						infolst.set(i, "k_"+ it+"(不匹配)");
					}
				}
			}else if(list.get(i).startsWith("o_")){
				if(ParseConfig.ontologyMap.containsKey(it)){
					String ontoname = ParseConfig.ontologyMap.get(it).toString();
					List<String> oexps =(List<String>) ParseConfig.ontoResource.get(ontoname);
					boolean matchflag =false;
					msg.stackflag=true;
					for(String exp:oexps)
					{
						String subcon = exp;//mdb.getConditionBysname(it);
						Map map = new HashMap<String, String>();
						map.putAll(cates);
						if(regexExpressionByCatelog(subcon,map,cutword)){
							msg.getOntology().add(it);
							matchflag=true;
							break;
						}
					}
					msg.stackflag=false;
					if(matchflag)
					{
						list.set(i, "true");
						infolst.set(i,"o_"+  it+"(匹配)");
					}else{
						list.set(i, "false");
						infolst.set(i, "o_"+ it+"(不匹配)");

					}
				}else{
					list.set(i, "false");
					infolst.set(i, "o_"+ it+"(不匹配)");
				}
			}else if(list.get(i).startsWith("y_")){
				String  tmpexp = (String)express.get(list.get(i));
				int size = cates.keySet().size();
				Set<Integer> set = cates.keySet();
				boolean flag = false ;
				msg.stackflag =true;
				for(Integer key :set){
					if(-1!=key){
						String value = (String)cates.get(key);
						Map expmap = new HashMap();
						expmap.put(-1, value);
						if(regexExpressionByCatelog(tmpexp,expmap,cutword)){
							flag=true;
							break;
						}
					}
				}

				if(flag){
					list.set(i, "true");
					infolst.set(i, msg.stackstr+"(匹配)");
				}else{
					list.set(i, "false");
					infolst.set(i, it+"(不匹配)");
				}
				msg.stackflag=false;
				msg.stackstr="";
			}else if(list.get(i).startsWith("z_")){
				String  tmpexp = (String)onlyexpress.get(list.get(i));
				int size = cates.keySet().size();
				boolean flag = false;
				if(size<3 && regexExpressionByCatelog(tmpexp,cates,cutword)){
					String tmpstr = (String) cates.get(-1);
					tmpstr = tmpstr.split("\\|")[1];
					String reg = cutword.toString().replaceAll(",", "|");
					reg = reg.replace("\\[|\\]", "").replaceAll(" ", "");
					tmpstr = tmpstr.replaceAll(reg, "");
					if(tmpstr.length()<5){
						flag = true;
					}
				}
				if(flag){
					list.set(i, "true");
					infolst.set(i, it+"(匹配)");
				}else{
					list.set(i, "false");
					infolst.set(i, it+"(不匹配)");
				}
			}


		}
		String tmp = "";
		if(seg.length>0){
			for (int i = 0; i < list.size(); i++) {
				tmp += (seg[i] + list.get(i));//把结果和操作字符拼接起来
				infostr += (seg[i] + infolst.get(i));
			}
			if(seg.length>list.size()){									
				tmp += seg[list.size()];											//有括号的话的加上最后的括号
				infostr+= seg[list.size()];
			}
		}else{
			tmp =list.get(0);
			infostr = infolst.get(0);
		}
		if(!msg.stackflag){
			msg.getMatchInfos().add("本体元素是: "+object+": "+infostr);
		}else{
			msg.stackstr="#"+infostr+"#";
		}
		boolean expressionResult = expressionEvaluator(tmp);
		
		
//		if(expressionResult && atFlag){
//			//获取元素值和概念值。
//			// c_1_38_002+c_1_38_002+ e_1_19_002+e_1_10_010  海尔的产品不错,相当好
//			catelog = catelog.replace("+ ","+");
//			System.out.println(catelog);
//			String s = catelog.substring(0,catelog.lastIndexOf(" ")-1);
//			String[] arrs = s.split("[+]");
//			
//			Set<String> set = new HashSet<String>();
//			for(String arr:arrs){
//				set.add(arr.trim());
//			}
//
//			ArrayList<String> all =  new ArrayList<String>(set);
//			
//			for(int i=0;i<itList.size();i++){
//				String enStr = itList.get(i);
//				if(enStr.startsWith("e_")){
//					itList.set(i, "e_"+ParseConfig.elementMap.get(enStr.replace("e_", "")));
//				}else if(enStr.startsWith("c_")){
//					itList.set(i, "c_"+ParseConfig.conceptMap.get(enStr.replace("c_", "")));
//				}
//			}
//			if(itList.size()==all.size()){
//				for(String str:all){
//					if(!itList.contains(str)){
//						return false;
//					}
//				}
//			}
//			
//			//@k_信用卡@，那么输入只有“信用卡”识别
//			if(!(regex.contains("e_")||regex.contains("c_"))){
//				if(all.size()!=0){
//					return false;
//				}
//			}
//			return true;
//
//		}
		return expressionResult;
	}

	/**
	 * 转换表达式
	 * 
	 * @param exp
	 * @return
	 */
	public boolean expressionEvaluator(String exp) {

		Boolean flag = false;
		exp = exp.replace("+", "&&");
		exp = exp.replace("|", "||");
		exp = exp.replace("-","&&!");
		if (StringUtils.isNotBlank(exp)) {
			try {
				flag =   (Boolean)ExpressionEvaluator.evaluate(exp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("表达式处理异常！",e);
			}
		}
		return flag;
	}


	public  boolean matchContent(String mode ,String content){
		Pattern pattern = Pattern.compile(mode);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			return true;
		}
		return false;
	}

	public static String getExpression(String word,Map  map){
		String reg ="#";
		List<String> strs = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(word, reg, true);
		while(tokenizer.hasMoreTokens()){
			String token =tokenizer.nextToken();
			strs.add(token);
		}
		int postion=0;
		for(int i=0;i<strs.size();i++){
			if(i-1>=0&&"#".equals(strs.get(i-1))&&i+1<strs.size()&&"#".equals(strs.get(i+1))){
				String tmp = strs.get(i);
				map.put("y_"+postion, tmp);
				strs.set(i, "y_"+postion);
				postion++;
				i=i+2;
			}


		}
		StringBuilder sb = new StringBuilder();
		for(String s:strs){
			if(!"#".equals(s)){
				sb.append(s);
			}
		}
		return sb.toString();
	}

	public static String getOnlyExpression(String word,Map  map){
		String reg ="@";
		List<String> strs = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(word, reg, true);
		while(tokenizer.hasMoreTokens()){
			String token =tokenizer.nextToken();
			strs.add(token);
		}
		int postion=0;
		for(int i=0;i<strs.size();i++){
			if(i-1>=0&&"@".equals(strs.get(i-1))&&i+1<strs.size()&&"@".equals(strs.get(i+1))){
				String tmp = strs.get(i);
				map.put("z_"+postion, tmp);
				strs.set(i, "z_"+postion);
				postion++;
				i=i+2;
			}

		}
		StringBuilder sb = new StringBuilder();
		for(String s:strs){
			if(!"@".equals(s)){
				sb.append(s);
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * @author 苏联兵
	 * 表达式位置验证的方法
	 */
	@SuppressWarnings("all")//压制警告
	public static String getExpressionLocation(String expression, Map map){
		/**
		 * 判断表达式参数是否为空和没有括号("{}")的情况。
		 */
		if(StringUtils.isBlank(expression)||expression.indexOf("{")<0)
			return expression;
		
		/**
		 * 数据初始化
		 */
		List<String> expressionList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int postion=0;
		
		/**
		 * 正则匹配获取需要的表达式数据，使用贪婪匹配模式。
		 */
		String regex = "\\{(.*?)\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(expression);
		while(matcher.find()){
			String tmpExpression = matcher.group();
			String expressionData = tmpExpression.replaceAll("\\{","").replaceAll("}","");
			map.put(postion,expressionData);
			expressionList.add("x_"+postion);
			postion++;
		}
		
		/**
		 * 将字符串先进行切分获取需要部分，把前面经过正则匹配的数据取出来进行拼接，返回拼接后的字符串表达式。
		 */
			String[] expressions = expression.split(regex);
			for (int i = 0; i < expressions.length; i++) {
				sb.append(expressions[i]+expressionList.get(i));
			}
			String expressionResult = sb.toString();
		return expressionResult;
	}

	public static void main(String[] args) {
		boolean b= new RegexExpression().expressionEvaluator("true+true-true");
		System.out.println(b);
	}
}
