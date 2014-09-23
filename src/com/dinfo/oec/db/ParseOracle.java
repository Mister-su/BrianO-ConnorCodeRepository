package com.dinfo.oec.db;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dinfo.init.DataBaseConfig;
import com.dinfo.init.DbConfig;
import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.MultiBTree;
import com.dinfo.oec.bean.ParseConfig;
import com.dinfo.oec.util.CommonUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ParseOracle implements ParseDb {
    private static final Logger log = Logger.getLogger(ParseOracle.class);
	private QueryRunner runner = DataBaseConfig.getQueryRunner();
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getConceptResByType(int)
	 */
	public Multimap<String, String> getConceptResByType(int type)
	{
		Multimap<String, String> res = ArrayListMultimap.create();
		String sql ="select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),concept_id) cate ,\"RESOURCE\"   from concept_resource"+DbConfig.suffix+" where type=? " +
				"  and concept_id is not null   and \"RESOURCE\" is not null   ";
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler(),type);
			for(Map<String,Object> map :list)
			{
				res.put(map.get("resource").toString(), map.get("cate").toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		return res; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getElementResByType(int)
	 */
	public Multimap<String, String> getElementResByType(int type)
	{
		Multimap<String, String> res = ArrayListMultimap.create();
		String sql ="select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),element_id) cate ,\"RESOURCE\"   from element_resource"+DbConfig.suffix+" where type=?  " +
				"  and element_id is not null   and \"RESOURCE\" is not null   ";
		
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler(),type);
			
			for(Map<String,Object> map :list)
			{
				res.put(map.get("resource").toString(), map.get("cate").toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		
		return res;
	}
	
	
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getOntologyMap()
	 */
	public BidiMap  getOntologyMap()
	{
		BidiMap res =  new DualHashBidiMap();
		String sql ="select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate ,ontology_name   from ontology"+DbConfig.suffix+"  " +
				"  where ontology_id is not null and    ontology_name is not null  ";
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			System.out.println("list:"+list.size());
			for(Map<String,Object> map :list)
			{
				if(!res.containsKey(map.get("ontology_name")))
					res.put( map.get("ontology_name").toString().trim(),map.get("cate").toString().trim());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getOntologyPath()
	 */
	public Map<String, String> getOntologyPath() {
		
		Map<String,String>  paths = new HashMap<String, String>();
		
		String sql ="select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate ,ontology_name   from ontology"+DbConfig.suffix+" " +
				"  where ontology_id is not null   and ontology_name is not null    order by ontology_level asc  ";
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			
			for(Map<String,Object> map :list)
			{
				String catename =map.get("ontology_name").toString().trim();
				String cateid =map.get("cate").toString().trim();
				if(cateid.length()>4){
					String tmp="";
					List<String> pcates = CommonUtil.getParentsByStr(cateid);
					for(String cate :pcates)
						tmp +="->"+ParseConfig.ontoName.get(cate);
					catename = tmp.substring(2);
				}
				paths.put(cateid,catename);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		
		return paths;
		
	}

	
	
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getElementWeight()
	 */
	public Map<String,Integer> getElementWeight()
	{
		Map<String,Integer> res = new HashMap<String, Integer>() ;
		String sql ="select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),element_id) cate ,element_weight  weight from element"+DbConfig.suffix+" " +
				"  where  element_id is not null  ";
		
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			
			for(Map<String,Object> map :list)
			{
				res.put(map.get("cate").toString() , (Integer)map.get("element_weight"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		
		return res;
	}
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getAllResource()
	 */
	public Set getAllResource() {
		String sql = "select  \"RESOURCE\" from concept_resource"+DbConfig.suffix+" where type =0"+
						" union"+ 
						" SELECT  \"RESOURCE\" from element_resource"+DbConfig.suffix+" where type =0";
		try {
			List<String> list = runner.query(sql,new ColumnListHandler<String>());
			return new HashSet<String>(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getIntentionExp()
	 */
	public Multimap<String, Integer> getIntentionExp(){
				
		Multimap<String, Integer> res = ArrayListMultimap.create();
		String sql = "SELECT  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate ," +
					"	intention   from ontology_resource"+DbConfig.suffix+" " +
					"  where ontology_id is not null  and  intention is not null and ontology_expression is not null   ";
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			
			for(Map<String,Object> map :list)
			{
				res.put(map.get("cate").toString() ,Integer.parseInt(map.get("intention").toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("",e);
			e.printStackTrace();
		}
		return res;
	}


	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getAllOntoExpresssion()
	 */
	public Multimap<String, String> getAllOntoExpresssion() {
		
		Multimap<String, String> res = ArrayListMultimap.create();
		String sql = "SELECT  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate ," +
					"	ontology_expression   from ontology_resource"+DbConfig.suffix+" " +
					"  where ontology_id is not null   and  ontology_expression is not null   ";
		
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			
			for(Map<String,Object> map :list)
			{
				res.put(map.get("ontology_expression").toString() , map.get("cate").toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("",e);
			e.printStackTrace();
		}
		
		return res;
	}



	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#geOntolongyElementMap()
	 */
	public Multimap<String, String> geOntolongyElementMap() {
		// TODO Auto-generated method stub
		String sql = " SELECT CONCAT(CONCAT(CAST(ontology_tree_id  AS CHAR),'_'),ontology_id) ontKey,CONCAT(CONCAT(CAST(element_tree_id AS CHAR),'_'),element_id) elemKey FROM ontology_element"+DbConfig.suffix+" " +
				" where element_id is not null   and ontology_id is not null  ";
		Multimap<String,String> res = ArrayListMultimap.create();
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());			
			for(Map<String,Object> map :list)
			{
				res.put(map.get("ontKey").toString() , map.get("elemKey").toString());
			}
		} catch (SQLException e) {
			log.error("",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

 

	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getAllConceptMap()
	 */
	public BidiMap getAllConceptMap() {
		BidiMap res = new DualHashBidiMap();
		String sql ="SELECT  concept_name  cate_name ,  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),concept_id) cate_id  from  concept"+DbConfig.suffix+" " +
				"  where concept_name is not null   and concept_id is not null  ";
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			
			for(Map<String,Object> map :list)
			{
				if(!res.containsKey(map.get("concept_name")))
					res.put(map.get("cate_name").toString() , map.get("cate_id").toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("",e);
			e.printStackTrace();
		}
		int size = res.size();
		return res;
		
	}


	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getAllElementMap()
	 */
	public BidiMap getAllElementMap() {
		BidiMap res = new DualHashBidiMap();
		String sql ="SELECT  element_name  cate_name ,  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),element_id) cate_id  from  element"+DbConfig.suffix+" " +
				" where element_name is not null   and element_id is not null  ";
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			
			for(Map<String,Object> map :list)
			{
				if(!res.containsKey(map.get("cate_name")))
					res.put(map.get("cate_name").toString() , map.get("cate_id").toString());
			}
		} catch (SQLException e) {
			log.error("",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}

	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getOntoDefaultCate()
	 */
	public Map<String, String> getOntoDefaultCate() {
		
		Map<String,String> res = new HashMap<String, String>() ;
		String sql ="SELECT  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'）,ontology_id) cate ,CONCAT(CONCAT('1','_'),default_ontology_id) ontoid from ontology"+DbConfig.suffix+"  " +
				 " where ontology_id is not null and default_ontology_id  is not null  ";
		
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			for(Map<String,Object> map :list)
			{	
				if(null!=map.get("cate")&& null!=map.get("ontoid"))
					res.put(map.get("cate").toString() , map.get("ontoid").toString());
			}
		} catch (SQLException e) {
			log.error("",e);
			e.printStackTrace();
		}
		return res;
	}
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getAllOntoIntentionExp(java.lang.String)
	 */
    public Multimap<String, String> getAllOntoIntentionExp(String ontId) {
        // TODO Auto-generated method stub
        Multimap<String, String> res = HashMultimap.create();
        String exp = "";
        Pattern p = null;
        Matcher m = null;
        String sql = "select ontology_expression from ontology_resource"+DbConfig.suffix+" where ontology_id=?";
        try {
             List<String> resList = runner.query(sql, new ColumnListHandler<String>(),ontId);
             if(resList != null){
                 for(int i =0;i<resList.size();i++){
                     exp = resList.get(i);
                     if(exp == null || "".equals(exp)){
                         continue;
                     }
                     p = Pattern.compile("(c_[\u4E00-\u9FFFa-z0-9A-Z]{1,50})|(k_[\u4E00-\u9FFFa-z0-9A-Z]{1,50})|(e_[\u4E00-\u9FFFa-z0-9A-Z]{1,50})");
                     m = p.matcher(exp);
                     while(m.find()){
                         String word = m.group();
                         if(word == null || "".equals(word)){
                             continue;
                         }
                         res.put(word.split("_")[1], exp);
                     }
                 }
             }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        	log.error("",e);
            e.printStackTrace();
        }
        return res;
    }
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getAllOntoExp()
	 */
    public Multimap<String, String> getAllOntoExp() {
        // TODO Auto-generated method stub
        Multimap<String, String> res = HashMultimap.create();
        String exp = "";
        Pattern p = null;
        Matcher m = null;
        String sql = "select ontology_expression from ontology_resource"+DbConfig.suffix+"";
        try {
             List<String> resList = runner.query(sql, new ColumnListHandler<String>());
             if(resList != null){
                 for(int i =0;i<resList.size();i++){
                     exp = resList.get(i);
                     if(exp == null || "".equals(exp)){
                         continue;
                     }
                     p = Pattern.compile("(c_[\u4E00-\u9FFFa-z0-9A-Z]{1,50})|(k_[\u4E00-\u9FFFa-z0-9A-Z]{1,50})|(e_[\u4E00-\u9FFFa-z0-9A-Z]{1,50})");
                     m = p.matcher(exp);
                     while(m.find()){
                         String word = m.group();
                         if(word == null || "".equals(word)){
                             continue;
                         }
                         res.put(word.split("_")[1], exp);
                     }
                 }
             }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("",e);
        }
        return res;
    }

	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getOntoWeight()
	 */
	public Map<String, Integer> getOntoWeight() {

		Map<String, Integer> res = new HashMap<String, Integer>();
		String sql = "select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate_id , weight  from ontology"+DbConfig.suffix+"  where   ontology_id is not null and weight is not null  ";
		try {
			List<Map<String, Object>> list = runner.query(sql,
					new MapListHandler());

			for (Map<String, Object> map : list) {
				res.put(map.get("cate_id").toString(), ((BigDecimal)map
						.get("weight")).intValue());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}

		return res;
	}

	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getJobItemByJobid(java.lang.String)
	 */
	public List<JobItem> getJobItemByJobid(String jobid)
	{
		String sql ="select  wo_no jobid ,content from wo_data"+DbConfig.suffix+" where  wo_no = ?";
		
		try {
			List<JobItem> items = runner.query(sql, new BeanListHandler<JobItem>(JobItem.class) ,jobid);
			return items;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getFilterWord()
	 */
	public String getFilterWord()
	{
		StringBuilder sb = new StringBuilder();
		String result="";
		String sql ="select word from word_filter"+DbConfig.suffix+" where word is not null and word<>''";
		try {
			List<String> words = runner.query(sql,new ColumnListHandler<String>());
			for(String word:words)
			{
				if(StringUtils.isNotBlank(word))
					sb.append(word+"|");
			}
			result =  sb.toString();
			if(StringUtils.isNotBlank(result))
			{
				result =result.substring(0, result.length()-1);
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("",e);
			e.printStackTrace();
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getintentionValue(java.util.List)
	 */
	public List<Integer>  getintentionValue(List<String[]> list) {
		// TODO Auto-generated method stub
		String sql = "SELECT intention FROM ontology_resource"+DbConfig.suffix+" where ontology_id = ? and ontology_expression = ?";
		List<Integer> result = new ArrayList<Integer>();
		try {
			for(String[] keyValue:list){
				String key = keyValue[0];
				key = key.substring(key.indexOf("_")+1);
				String value = keyValue[1];
				result.addAll(runner.query(sql, new ColumnListHandler<Integer>(),key,value));				
			}
		} catch (SQLException e) {
			log.error("",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getConceptName()
	 */
	public List<String> getConceptName(){
		List<String> list = null;
		String sql = "select concept_name from concept"+DbConfig.suffix+"";
		try {
			list = runner.query(sql, new ColumnListHandler<String>());
		} catch (SQLException e) {
			log.error("",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getElementName()
	 */
	public List<String> getElementName(){
		List<String> list = null;
		String sql = "select element_name from element"+DbConfig.suffix+"";
		try {
			list = runner.query(sql, new ColumnListHandler<String>());
		} catch (SQLException e) {
			log.error("",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.dinfo.oec.db.ParseD#getOntologyExpression()
	 */
	public List<String> getOntologyExpression(){
		List<String> list = null;
		String sql = "select ontology_expression from ontology_resource"+DbConfig.suffix+"";		
		try {
			list = runner.query(sql, new ColumnListHandler<String>());
		} catch (SQLException e) {
			log.error("",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}


	public MultiBTree getOntoLevelTree()
	{
		MultiBTree  btree = new MultiBTree("0_0",null);
		
		String sql ="select CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate_id," +
				" CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_parent_id)  pcate_id  , ontology_level  from   " +
				"  ontology"+DbConfig.suffix+" ORDER BY ontology_level";
		try {
			List<Map<String,Object>> data = runner.query(sql, new MapListHandler());
			if(CollectionUtils.isNotEmpty(data))
			{
				for(Map<String,Object> map :data)
				{	
					String cate_id =map.get("cate_id").toString();
					int level = Integer.parseInt(map.get("ontology_level").toString());
					String pcate_id=map.get("pcate_id").toString();
					if(level<0)
						pcate_id ="0_0";
					MultiBTree treenode = new MultiBTree(cate_id, pcate_id);
					btree.addTreeNode(treenode);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return btree;
		
	}


	/**
	 * 
	 * 获取所有的 cate_id 和本体资源的信息
	 *
	 */
	@Override
	public Multimap<String, String> getAllOntoResource() {
		
		Multimap<String, String> result = ArrayListMultimap.create();
		String sql = "select CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate_id, ontology_expression \"RESOURCE\" " +
				"  from ontology_resource"+DbConfig.suffix+" ";		
		try {
			List<Map<String,Object>> datas = runner.query(sql, new MapListHandler());
			if(CollectionUtils.isNotEmpty(datas))
			{
				for(Map<String,Object> map :datas)
				{
					String cateid =map.get("cate_id").toString();
					String exp = map.get("resource").toString();
					result.put(cateid, exp);
				}
			}
		} catch (SQLException e) {
			log.error("",e);
			e.printStackTrace();
		}
		return result;
	}


	@Override
	public List<String> getWordbase() {
		String sql ="select  word from word_base where word is not null";
		try {
			return  runner.query(sql,new ColumnListHandler<String>());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<String> getWorduserByType(int type) {
		String sql="select word from word_user where sign=? and word is not null";
		try {
			return runner.query(sql, type, new ColumnListHandler<String>());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Map<String, String> getInnerCodes() {
		Map<String,String> result = new HashMap<String, String>();
		String sql=" select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate , CONCAT(CONCAT(inner_code,'|'),CAST(is_share AS CHAR)) innercode " +
		"from ontology"+DbConfig.suffix +"   ";
		try {
			List<Map<String,Object>> datas = runner.query(sql, new MapListHandler());
			if(CollectionUtils.isNotEmpty(datas))
			{
				for(Map<String,Object> map :datas)
				{
					String cateid =map.get("cate")!=null ? map.get("cate").toString():"0";
					String exp = map.get("innercode")!=null? map.get("innercode").toString():"0";
					result.put(cateid, exp);
				}
			}
		} catch (SQLException e) {
			log.error("",e);
			e.printStackTrace();
		}
		return result;	
	}


	@Override
	public Map<String, String> getOntoNames() {
		Map<String,String> res =  new HashMap<String, String>();;
		String sql ="select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate ,ontology_name   from ontology"+DbConfig.suffix+"  " +
				"  where ontology_id is not null and    ontology_name is not null  ";
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			if(CollectionUtils.isEmpty(list))
				return res;
			for(Map<String,Object> map :list)
			{
			
				res.put( map.get("cate").toString().trim(),map.get("ontology_name").toString().trim());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		return res;
	}


	@Override
	public MultiKeyMap getAllOntoResFactors() {
		String sql ="select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate , ontology_expression , factor   from ontology_resource"+DbConfig.suffix+"  " +
		"  where ontology_id is not null and    ontology_expression is not null  ";
		
		MultiKeyMap  fmap = new MultiKeyMap();
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			if(CollectionUtils.isEmpty(list))
				return fmap;
			for(Map<String,Object> map :list)
			{
				String cate = map.get("cate").toString().trim();
				String expression =map.get("ontology_expression").toString().trim();
				String factor =map.get("factor")==null? "0" :map.get("factor").toString().trim();
				fmap.put( cate,expression,Integer.parseInt(factor));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		return fmap;
		
	}


	@Override
	public Map<String, Integer> getAllpfilters() {
		
		Map<String,Integer> res =  new HashMap<String, Integer>();;
		String sql ="select  CONCAT(CONCAT(CAST(tree_id AS CHAR),'_'),ontology_id) cate ,pfilter   from ontology"+DbConfig.suffix+"  " +
				"  where ontology_id is not null and    pfilter is not null  ";
		try {
			List<Map<String,Object>> list = runner.query(sql,new MapListHandler());
			if(CollectionUtils.isEmpty(list))
				return res;
			for(Map<String,Object> map :list)
			{
				if(null!=map.get("pfilter"))
					res.put( map.get("cate").toString().trim(),Integer.parseInt(map.get("pfilter").toString().trim()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("",e);
		}
		return res;
		
	}
}
