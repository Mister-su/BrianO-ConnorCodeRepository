package com.dinfo.oec.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.dinfo.init.DataBaseConfig;
import com.dinfo.oec.bean.JobItem;

public class ParseTestDb {
	
	private QueryRunner runner = DataBaseConfig.getQueryRunner();
	
	
	public  List<String>  getjobidBycate(String pcate ,String cate)
	{
		String sql  ="select DISTINCT wo_no  from wo_data where wo_cate =?  and wo_reason=? and status=0";
		
		try {
			List<String> list =  runner.query(sql, new ColumnListHandler<String>(),pcate,cate);
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public  List<String>  getjobidBypcate(String pcate )
	{
		String sql  ="select DISTINCT wo_no  from wo_data where wo_cate =?  and status=0 ";
		
		try {
			List<String> list =  runner.query(sql, new ColumnListHandler<String>(),pcate);
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<JobItem> getJobItemByJobid(String jobid)
	{
		String sql ="select  wo_no jobid ,content from wo_data where  wo_no = ?";
		
		try {
			List<JobItem> items = runner.query(sql, new BeanListHandler<JobItem>(JobItem.class) ,jobid);
			return items;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<JobItem> getAllItems()
	{
		String sql ="select  wo_no jobid ,content from wo_data where wo_reason not LIKE '%;%' ";
		
		try {
			List<JobItem> items = runner.query(sql, new BeanListHandler<JobItem>(JobItem.class));
			return items;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<String[]> getpath()
	{
		List<String[]> result = new ArrayList<String[]>();
		String sql ="SELECT DISTINCT wo_cate pcate  ,wo_reason cate from wo_data where wo_reason not LIKE '%;%'  ORDER BY wo_cate  ";
		try {
			List<Map<String,Object>> list = runner.query(sql, new MapListHandler());
			for(Map<String,Object> map :list)
			{
				if(MapUtils.isNotEmpty(map))
				{
					String[] str=new String[2];
					if(map.get("cate").toString().indexOf(";")!=-1)
					{
						String[] segs  = map.get("cate").toString().split(";");
						for(String seg :segs){
							String[] cats = new String[2];
							cats[0]=map.get("pcate").toString();
							cats[1]=seg;
							result.add(cats);
						}
					}else{
						str[0]= map.get("pcate").toString();
						str[1]= map.get("cate").toString();
					}
					result.add(str);
				}
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getppath()
	{
		String sql ="SELECT DISTINCT wo_cate pcate   from wo_data ORDER BY wo_cate";
		try {
			List<String> list = runner.query(sql, new ColumnListHandler<String>());
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
