package com.dinfo.test;

import java.io.File;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.dinfo.init.DataBaseConfig;
import com.dinfo.init.ResourceInit;

public class Createseqtiger {
	
	
	static{
		init();
	}
	
	public static void creatseq(String name)
	{
		QueryRunner runner =DataBaseConfig.getQueryRunner();
		String ssql ="create sequence "+ name+"_seq  increment " +
				" by 1 start with 1 minvalue 1 maxvalue 999999999 nocache order;";
		System.out.println(ssql);
	
//		try {
//			runner.update(tsql);
//			runner.update(ssql);
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static void createtigger(String name)
	{
		String tsql="create or replace  trigger "+ name+"_trigger before insert on "+name+ 
		" for each row "+ 
		" begin "+ 
		" select "+name+"_seq.nextval into:new.id from sys.dual ;"+ 
		" end;";
		System.out.println(tsql);		
	}
	
	
	public static void main(String[] args) {
//		init();
		String tabs="concept,concept_beta,concept_resource,concept_resource_beta,element,element_beta,element_resource,element_resource_beta,exam_data,exam_data_beta,exam_expression,exam_expression_beta,exam_word,exam_word_beta,express_analy,langue,langue_beta,ontology,ontology_beta,ontology_element,ontology_element_beta,ontology_propery,ontology_propery_beta,ontology_propery_relation,ontology_propery_relation_beta,ontology_propery_resource,ontology_propery_resource_beta,ontology_resource,ontology_resource_beta,seed_word,seed_word_beta,t_auth,t_auth_beta,t_role,t_role_beta,t_user,t_user_beta,version,word_filter,word_filter_beta";
		tabs = tabs.toUpperCase();
		String[] tabss = tabs.split(",");
//		for(String key :tabss)
//			creatseq(key);
		for(String key :tabss)
			createtigger(key);
		
		
	}
	
	public static void init()
	{
		File file = new File("");
		String datapath=file.getAbsolutePath();
		String configpth=datapath+"/config/db.properties";
		ResourceInit.initconfig(configpth, datapath);
		DataBaseConfig.init();
	}
}
