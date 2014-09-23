package com.dinfo.oec.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class Utils {

	/**
	 * 获取当前时间的字符串
	 * @return
	 */
	public static String getTimeNowstr(){
		return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String getNTime(int interval){
		long time = (new Date()).getTime()- (interval*60*60*1000);
		Date  last = new Date(time);
		return DateFormatUtils.format(last, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String timestrToSolrTimestr(String time){
		time =time.substring(0,time.length()-2);
		String solrtime = time.replace(" ", "T")+"Z";
		return solrtime;
	}
	
	public static String dateToStr(Date date){
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String toZone(String timestr){
		timestr = timestr.replace(" ", "T");
		return timestr+"Z";
	}
	
	public static String getNMinu(int interval){
		long time = (new Date()).getTime()+ (interval*60*1000);
		Date  last = new Date(time);
		return DateFormatUtils.format(last, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static  void writeObject(Object o,String file){
		try {
			FileOutputStream fout = new FileOutputStream(new File("./obj/"+file));
			ObjectOutputStream  out = new ObjectOutputStream(fout);
			out.writeObject(o);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeOjectList(List os ,String file){
		for(Object o:os){
			writeObject(o,file);
		}
	}
	
	public static  Object readObject(String file){
		Object o =null;
		try {
			FileInputStream fin = new FileInputStream(new File("./obj/"+file));
			ObjectInputStream in = new ObjectInputStream(fin);
			o = in.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	
	public static  boolean patternTime(String word){
		if(null==word){
			return false;
		}
		String p1="(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
		String p2 ="((19|20)\\d\\d)年(0?[1-9]|1[012])月(0?[1-9]|[12][0-9]|3[01])日";
		String p3 = "从(((((19|20)\\d\\d)年)?((0?[1-9]|1[012])月)?((0?[1-9]|[12][0-9]|3[01])日)?)|(((一九|二零)[一二三四五六七八九零]{2})年(([一二三四五六七八九]|一?十([一二])?)月)?(([一二三四五六七八九]|一?十[一二三四五六七八九]|二十[一二三四五六七八九]|三十一?)日)?))到((((19|20)\\d\\d)年((0?[1-9]|1[012])月)?((0?[1-9]|[12][0-9]|3[01])日)?)|(((一九|二零)[一二三四五六七八九零]{2})年(([一二三四五六七八九]|一?十([一二])?)月)?(([一二三四五六七八九]|一?十[一二三四五六七八九]|二十[一二三四五六七八九]|三十一?)日)?))";
		String p4 = "((((19|20)\\d\\d)年((0?[1-9]|1[012])月)?((0?[1-9]|[12][0-9]|3[01])日)?)|(((一九|二零)[一二三四五六七八九零]{2})年(([一二三四五六七八九]|一?十([一二])?)月)?(([一二三四五六七八九]|一?十[一二三四五六七八九]|二十[一二三四五六七八九]|三十一?)日)?))以前";
		String p5 = "((((19|20)\\d\\d)年((0?[1-9]|1[012])月)?((0?[1-9]|[12][0-9]|3[01])日)?)|(((一九|二零)[一二三四五六七八九零]{2})年(([一二三四五六七八九]|一?十([一二])?)月)?(([一二三四五六七八九]|一?十[一二三四五六七八九]|二十[一二三四五六七八九]|三十一?)日)?))以后";
		String p6 = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0?[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0?[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
		String p7="(([1-9])|(0[1-9])|(1[0-2]))/(([0-9])|([0-2][0-9])|(3[0-1]))/(([0-9][0-9])|([1-2][0,9][0-9][0-9]))";
		String p = "("+p1+")|("+p2+")|("+p3+")|("+p4+")|("+p5+")|("+p6+")|("+p7+")";
		Pattern pattern = Pattern.compile(p);
		Matcher matcher = pattern.matcher(word);
		if(matcher.find()){
			return true;
		}
		
		return false;
	}
	
	public static void LogInfo(String info ,String tag){

			File filetmp = new File("./log");
			if(!filetmp.exists()){
				filetmp.mkdir();
			}
			String path = filetmp.getPath()+"/analy.log";
			File file = new File(path);
			FileWriter fw = null;
			try {
				fw = new FileWriter(file, true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			PrintWriter out = new PrintWriter(fw);
			out.println("------------------------------------------------"
					+ tag
					+ "-----------------------------------------------------");
			out.println();
			out.flush();
			out.println(info);
			out.flush();
			try {
				fw.flush();
				fw.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
	
	public static void printLog(List<String> strs, String tag) {
		if (null != strs) {
			
			File filetmp = new File("./log");
			if(!filetmp.exists()){
				filetmp.mkdir();
			}
			String path = filetmp.getPath()+"/analy.log";
			File file = new File(path);
			FileWriter fw = null;
			try {
				fw = new FileWriter(file, true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			PrintWriter out = new PrintWriter(fw);
			out.println("------------------------------------------------"
					+ tag
					+ "-----------------------------------------------------");
			out.println();
			out.flush();
			for(String str:strs){
				out.println(str);
				out.flush();
			}
			try {
				fw.flush();
				fw.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isContainInList(String word ,List<String> names){
		
		if(null == word || null==names){
			return false;
		}
		for(String name :names){
			if(word.indexOf(name)!=-1){
				return true;
			}
		}
		
		return false;
	}
	
	public static String splitAndword(String word){
		StringBuilder sb = new StringBuilder();
		String[] seg = word.split(" ");
		for(String s :seg){
			if(StringUtils.isNotBlank(s)){
				sb.append("  "+ s+"  AND");
			}
		}
		String result=null;
		result = sb.toString();
		result = result.substring(0,result.length()-3);
		return result;
	}
	
	public static String splitOrword(String word){
		StringBuilder sb = new StringBuilder();
		String[] seg = word.split(" ");
		for(String s :seg){
			if(StringUtils.isNotBlank(s)){
				sb.append("  "+ s+"  OR");
			}
		}
		String result=null;
		result = sb.toString();
		result = result.substring(0,result.length()-2);
		return result;
	}
	
	public static Map<String,List<String>> segStrList(String s){
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		String[] seg = s.split(";");
		for(String str :seg){
			if(StringUtils.isNotBlank(str)){
				list.add(str);
			}
		}
		
		for(String str:list){
			map.put(str, list);
		}
		return map;
	}

	public static boolean isMatchSameWord(List<String> samewords , String content){
		if(CollectionUtils.isEmpty(samewords)){
			return false;
		}
		for(String s :samewords){
			if(content.indexOf(s)!=-1){
				return true;
			}
		}
		return false;
	}
	
	public static List<String> SegWordList(String word){
		List<String> resList  =new ArrayList<String>();
		if(StringUtils.isNotBlank(word)){
			String[] seg = word.split(";");
			resList.addAll(Arrays.asList(seg));
		}
		return resList;
	}
	
	public static String[] splitToSentence(String word){
		String patten ="\\.|\\?|\\!|\\？|\\！|\\。";
		String seg[] =word.split(patten);
		Pattern pattern = Pattern.compile(patten);
		Matcher matcher = pattern.matcher(word);
		int i=0;
		while(matcher.find()){
			if(i<seg.length){
				seg[i]=seg[i]+matcher.group(0);
				i++;
			}else{
				break;
			}
		}
		return seg;
	}
	
	public static Date strToDate(String datestr){
		if(StringUtils.isBlank(datestr)){
			return new Date(0);
		}
		Date date = null;
		try {
			datestr =datestr.substring(0,datestr.length()-2);
			date = DateUtils.parseDate(datestr, new String[]{"yyyy-MM-dd HH:mm:ss"});
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static String zoneTimeToTimestr(String zonetime){
		return zonetime.replace("T", " ").substring(0,zonetime.length()-1);
	}
	
	public static String list2String(List<String> list){
		StringBuilder sb = new StringBuilder();
		for(String s:list){
			sb.append(s);
		}
		return sb.toString();
	}
	
	public static String DateToStr(Date date){
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	
	public  static List<Integer>   segStrToint(String str){
		List<Integer> ids  = new ArrayList<Integer>();
		String[] seg = str.split("\\|");
		for(String s :seg){
			if(StringUtils.isNotBlank(s)){
				ids.add( Integer.valueOf(s));
			}
		}
		return ids;
	}
	
	public static List<String> segStrls(String str){
		List<String>  list =  new ArrayList<String>();
		String[] seg = str.split("\\||\\;|\\,| ");
		for(String s :seg){
			if(StringUtils.isNotBlank(s)){
				list.add(s);
			}
		}
		return list;
	}
	
	public static  boolean isContain(String content ,String  keyword){
		 if(content.indexOf(keyword)!=-1){
			 return  true;
		 }
		 return false;
	}
	// 字符串分成 四段
	public static String[] segStrToArray(String word)
	{	
		int length = word.length()/4;
		if(StringUtils.isBlank(word)){
			return null;
		}
		String[] seg = new String[4];
		for(int i=0;i<4;i++)
			seg[i]=word.substring(i*length, (i+1)*length);
		return seg;
	}
	
	public static  List<String> jiaoji(List<String> ...args){
		List<String> list = new ArrayList<String>();
		if(args.length>0){
			list.addAll(args[0]);
		}
		for(int i=1 ;i<args.length;i++){
			list.retainAll(args[i]);
		}
		return list;
	}
	


	
	/**
	 * 把几个分成多页
	 * @param <T>
	 * @param list
	 * @param page
	 * @return
	 */
	public  static <T> List<List<T>> segList(List<T>  list , int page){
		List<List<T>> result = new ArrayList<List<T>>();
 		if(null==list){
			return null;
		}
		int size = list.size();
		if(page>size){
			for(T t:list){
				List<T> ts = new ArrayList<T>();
				ts.add(t);
				result.add(ts) ;
			}
			return result;
		}
		double dsize =(double)size;
		int count = (int)Math.ceil((dsize/page));
		for(int i =0;i<page;i++){
			int start = i*count;
			int end ; 
			end =(i+1)*count;
			if(end<size ){
				List<T> templist = new ArrayList<T>(list.subList(start, end));
				result.add(templist);
			}else if(start<size){
				List<T> templist = new ArrayList<T>(list.subList(start, size));
				result.add(templist);
				break;
			}else{
				break;
			}
		}
		return result;
	}
	
	public  static <T> List<List<T>> segListCount(List<T>  list , int count){
		List<List<T>> result = new ArrayList<List<T>>();
 		if(null==list){
			return null;
		}
		int size = list.size();
		if(count>size){
				result.add(list) ;
			return result;
		}
		double dsize =(double)size;
		int page = (int)Math.ceil((dsize/count));
		for(int i =0;i<page;i++){
			int start = i*count;
			int end ; 
			end =(i+1)*count;
			if(end<size ){
				List<T> templist = new ArrayList<T>(list.subList(start, end));
				result.add(templist);
			}else if(start<size){
				List<T> templist =new ArrayList<T>( list.subList(start, size));
				result.add(templist);
				break;
			}else{
				break;
			}
		}
		return result;
	}
	
	
	public static <T> T randomObj(List<T> ts){
		Random random = new Random(System.currentTimeMillis());
		T t = null;
		if(CollectionUtils.isNotEmpty(ts)){
			int size = ts.size();
			int pos = (Math.abs(random.nextInt())%size);
			t=ts.get(pos);
		}
		return t;
	}
	
	
	public static String[] getTengTime(String start_time ,String end_time)
	{
		if(StringUtils.isBlank(start_time)|| StringUtils.isBlank(end_time)){
			return null;
		}
		String[] time = new String[4];
		try {
			String start = DateUtils.parseDate(start_time, new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime()+""; 
			start =StringUtils.chomp(start, "000");
			String end = DateUtils.parseDate(end_time, new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime()+""; 
			end =StringUtils.chomp(end, "000");
			time[0]=start;
			time[1]=end;
			time[2]=start_time;
			time[3]=end_time;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	public static String[] getSinaTime(String start_time ,String end_time)
	{
		if(StringUtils.isBlank(start_time)|| StringUtils.isBlank(end_time)){
			return null;
		}
		start_time =start_time.replace(" ", "-");
		end_time = end_time.replace(" ", "-");
		String time = start_time.substring(0,start_time.length()-6)+":"+end_time.substring(0,end_time.length()-6);
		String[] timearr = new String[3];
		timearr[0]=time;
		timearr[1]=start_time;
		timearr[2]=end_time;
		return timearr;
	}
	
public static boolean isTypeofCollecton(Class type){
		
		String name = type.getName().toLowerCase();
		String patternstr ="set|list";
		Pattern pattern = Pattern.compile(patternstr);
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()){
			return true;
		}
		return false;
	}
	public static boolean isTypeofMap(Class type){
		
		String name = type.getName().toLowerCase();
		String patternstr ="map";
		Pattern pattern = Pattern.compile(patternstr);
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()){
			return true;
		}
		return false;
	}
	public static boolean isTypeofString(Class type){
		
		String name = type.getName().toLowerCase();
		String patternstr ="string";
		Pattern pattern = Pattern.compile(patternstr);
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()){
			return true;
		}
		return false;
	}
	
	public static String zoneTime(String time)
	{
		time = time.replaceAll(" ","T");
		return time+"Z";
	}
	
	public static int getPageStart(int page , long size ,int pagesize)
	{
		int start = 0;
		double pagecount = Math.ceil((double)size/pagesize);
		if(page > pagecount)
		{
			start = (int)(size-pagesize);
		}else{
			start = (page-1)*pagesize;
		}
		start = start<0 ? 0: start;
		return start;
	}
	
	

	public  static String  concatWithPrefix(ArrayList<String> cates , String prefix)
	{
		if(CollectionUtils.isEmpty(cates))return null;
		StringBuilder sb = new StringBuilder();
		for(String cate :cates)
		{
			if(StringUtils.isNotBlank(cate)){
				sb.append(prefix+"_"+cate+"+");
			}
		}
		String result = sb.toString();
		return  result.substring(0,result.length()-1);
		
	}
	/**
	 * 集合里面的每个元素都加上前缀
	 * @param cates		分类名字
	 * @param prefix	前缀
	 * @return
	 */
	public static List<String> concatListWithPrefix(List<String> cates , String prefix)
	{
		if(CollectionUtils.isEmpty(cates))return null;
		List<String> result  =new ArrayList<String>();
		for(String cate :cates)
		{
			if(StringUtils.isNotBlank(cate)){
				String temp =prefix+cate;
				result.add(temp);
			}
		}
		return  result;
	}
	
	/**
	 * 集合里面的每个元素都加上前缀
	 * @param cates		分类名字
	 * @param end	后缀
	 * @return
	 */
	public  static String  concatWithEnd(Collection<String> cates , String end)
	{
		if(CollectionUtils.isEmpty(cates))return null;
		StringBuilder sb = new StringBuilder();
		for(String cate :cates)
		{
			if(StringUtils.isNotBlank(cate)){
				sb.append(cate+end);
			}
		}
		String result = sb.toString();
		if(StringUtils.isNotBlank(result)&&result.length()>0)
			result = result.substring(0,result.length()-1);
		return  result;
		
	}
	
	public  static String  concat(List<String> cates )
	{
		if(CollectionUtils.isEmpty(cates)) return null;
		StringBuilder sb = new StringBuilder();
		for(String cate :cates)
		{
			if(StringUtils.isNotBlank(cate)){
				sb.append(" "+cate+"+");
			}
		}
		String result = sb.toString();
		return  result.substring(0,result.length()-1);
	}

	public static <E>  void  addCollection(Collection<E> tobj , Collection<E> sobj)
	{
		if(CollectionUtils.isNotEmpty(sobj)&&tobj!=null)
			tobj.addAll(sobj);
	}
	
	
	public static <k,v> List<k>  sortMapKey(Map<k,v> map)
	{
		List<k> keys = new ArrayList<k>();
		List<Map.Entry<k, v>> list =   new ArrayList<Map.Entry<k, v>>(map.entrySet());
		Collections.sort(list, new Comparator<Entry<k, v>>(){

			public int compare(Entry<k, v> mapsrc, Entry<k, v> mapto) {
				// TODO Auto-generated method stub
				return (int)((Double)mapsrc.getValue()-(Double)mapto.getValue());
			}
		});
		
		for(Map.Entry<k, v> entry  : list)
		{
			keys.add(entry.getKey());
		}
		return keys;
	}
 	
}


