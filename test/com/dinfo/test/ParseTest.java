package com.dinfo.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dinfo.init.DataBaseConfig;
import com.dinfo.init.ResourceInit;
import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.MultiBTree;
import com.dinfo.oec.bean.ParseConfig;
import com.dinfo.oec.bean.ParseInfo;
import com.dinfo.oec.bean.StatusMsg;
import com.dinfo.oec.db.ParseDb;
import com.dinfo.oec.db.ParseMySql;
import com.dinfo.oec.db.ParseOracle;
import com.dinfo.oec.main.OECParseMain;
import com.dinfo.oec.util.CommonUtil;
import com.dinfo.oec.util.RegexExpression;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ParseTest {

	 
	static{
		init();
	}
	
	public static void main(String[] args) {
		//List<JobItem> items = new ArrayList<JobItem>();
		//JobItem item = new JobItem("001", "0212", null, null, "131762925827寄方13751861013林R 来电，对此件时效非常不满，解析不接受，称收方明天一早的航班离开，要求明天第一时间核实派送时间复至本机并就此件延误给其一个满意答复，请跟进，谢谢，0208328  ");
		JobItem item = new JobItem();
		//item.setJobid("001");
		item.setContent("复制卡");
		//items.add(item);
		/*JobItem item2= new JobItem("002", "012", null, null, "知会寄方*********** 聂小姐表示收方联货款都没支付给其，不愿意授权给收方处理，寄方表示其会与收方做好解释，后续我司联系其处理即可，已告知我司与其处理好，客无异议***秋雨");
		items.add(item2);*/
		OECParseMain parseMain = new OECParseMain();
		//ParseInfo info = parseMain.parseItemList(items, null,0);
		ParseInfo info = parseMain.parseItem(item,null,0);
		Map<String,ArrayList<String>> map = info.getConceptValueMap();
		
		//System.out.println(map.get("负面"));
		Map<String,ArrayList<String>> elemap = info.getElementValueMap();
		System.out.println("bbbb");
		/*System.out.println(elemap.get("银行"));
		System.out.println(elemap.get("卡"));
		System.out.println(elemap.get("动作"));
		*/
//		ParseInfo info = new ParseInfo();
//		List<String> cates = new ArrayList<String>();
//		cates.add("1_10_001_002");
//		info.setCatlogs(cates);
//		CommonUtil.innerCodeShared(info);
		
//		ParseDb db = new ParseMySql();
//		List<String> data =db.getWordbase();
//		System.out.println(data.toString());
		
	}
	
	
	public static void testdb()
	{
		
		
		ParseDb db  =new ParseOracle();
		
		
		MultiBTree tree = db.getOntoLevelTree();
		
		System.out.println(tree.getChildStrByLevel("5_35", 3));
	}

	public static void init()
	{
		File file = new File("");
		String datapath=file.getAbsolutePath();
		String configpth=datapath+"/config/db.properties";
		ResourceInit.initconfig(configpth, datapath);
		ResourceInit.init();
	}
	
	
	
}
