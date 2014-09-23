package com.dinfo.oec.main;

import java.util.List;

import com.dinfo.init.ResourceInit;
import com.dinfo.oec.bean.JobGroup;
import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.ParseInfo;
import com.dinfo.oec.common.ParseCommon;

public class OECParseMain {
	
	private ParseCommon common =new ParseCommon();
	
	/**
	 * 处理单条记录，
	 * @param item 	要处理的数据
	 * @return
	 */
	public  ParseInfo parseItem(JobItem item ,String pcate_id ,int level)
	{
		return common.parseJobItem(item,pcate_id,level);
	}
	
	/**
	 * 批量处理一个分组集合
	 * @param items		一个分组集合
	 * @return
	 */
	public  ParseInfo parseItemList(List<JobItem> items ,String pcate_id, int level)
	{
		System.out.println(items.size());
		JobGroup group = new JobGroup("001",items);
		return common.parseJobGroup(group,pcate_id,level);
	}
	
	/**
	 * 
	 * @param items
	 * @param pcate_id
	 * @param level
	 * @return
	 */
	public ParseInfo parseItemListForOntology(List<JobItem> items ,String pcate_id, int level)
	{
		return common.parseJobItemListInorder( items ,pcate_id,level);
	}
	
	/**
	 * 
	 * @param items
	 * @param pcate_id
	 * @param level
	 * @return
	 */
	public ParseInfo parseItemForOntology(JobItem item ,String pcate_id, int level)
	{
		return common.parseJobItemForOntology( item ,pcate_id,level);
	}
	
	/**
	 * 重新加载资源
	 */
	public static void   reloadResource()
	{
		ResourceInit.loadResource();
	}
	
	

	
}
