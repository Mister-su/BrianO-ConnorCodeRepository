package com.dinfo.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public class DataBaseConfig {
    private static final Logger log = Logger.getLogger(DataBaseConfig.class);
	
	public DataBaseConfig() {
		init();
	}
	
	/**
	 * 资源初始化方法
	 */
	public static void init() {
										//读取配置文件设置参数
		BasicDataSource source = new BasicDataSource();		
		source.setUsername(DbConfig.username);
		source.setPassword(DbConfig.password);
		String url =DbConfig.dburl;
		if(DbConfig.dbtype.equals("mysql"))
			url = DbConfig.dburl+"&user="+DbConfig.username+"&password="+DbConfig.password;
		source.setUrl(url);
		source.setDriverClassName(DbConfig.driverclass);
		source.setMaxActive(80);
		source.setMaxIdle(20);
		source.setMaxWait(20);
		DbConfig.ds = (DataSource)source;				//设置数据源
//		initFactory();

	}

	/**
	 * 读取配置文件
	 */
	public static void initPropertis(String path) {
		FileInputStream fin;
		try {
			FileInputStream in  = new FileInputStream(new File(path));
			Properties pro = new Properties();
			pro.load(in);
			DbConfig.dburl = pro.getProperty("dburl");
			DbConfig.username = pro.getProperty("username");
			DbConfig.password = pro.getProperty("password");
			DbConfig.driverclass = pro.getProperty("driverclass");
			DbConfig.dbtype = pro.getProperty("dbtype");
			DbConfig.suffix= pro.getProperty("suffix");
			//设置处理线程数。
//			DbConfig.threadcount = Integer.parseInt(pro.getProperty("threadcount"));
			//设置服务端口
//			DbConfig.serverport = Integer.parseInt(pro.getProperty("serverport"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("配置文件没有找到！",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static QueryRunner getQueryRunner(){
		return new QueryRunner(DbConfig.ds);
	}
	
	
	
//	public static void initFactory(){
//		Reader reader = null;
//		try {
//			reader = Resources.getResourceAsReader("mybatis-config.xml");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			log.error("mybatis-config.xml读取错误！",e);
//			e.printStackTrace();
//		}
//		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//		DbConfig.factory = builder.build(reader);
//	}
	
	
}
