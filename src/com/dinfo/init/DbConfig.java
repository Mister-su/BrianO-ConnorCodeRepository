package com.dinfo.init;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.ibatis.session.SqlSessionFactory;

public class DbConfig {

	public static String dburl; // 数据库url

	public static String username; // 数据库用户名

	public static String password; // 数据库密码

	public static String driverclass; // 数据库驱动
	
	public static Integer threadcount;


	public static DataSource ds;
	
	public static MultiKeyMap sendlimit;
	
	public static List<String> stoken;
	
	public static SqlSessionFactory factory;
	
	//mongodb相关配置
	public static String mongodbaddr ;
	public static Integer mongodbport;
	public static String mongodbbasename ;
	
	//server 配置
	public static int serverport;
	
	public static String dbtype;
	
	public static String suffix;
	
	
	
}
