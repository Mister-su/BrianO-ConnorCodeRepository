package com.dinfo.init;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

public class DBCPDataSourceFactory implements DataSourceFactory{
	
	private BasicDataSource  dataSource = new BasicDataSource();
	
	public DBCPDataSourceFactory() {
		this.dataSource = new BasicDataSource();
	}

	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		return this.dataSource;
	}

	public void setProperties(Properties ps) {
		dataSource.setDriverClassName( ps.getProperty("driverclassname"));
		dataSource.setUsername( ps.getProperty("username"));
		dataSource.setUrl( ps.getProperty("url"));
		dataSource.setPassword( ps.getProperty("password"));
		dataSource.setDefaultAutoCommit( ps.getProperty("defaultautocommit","0").equals("1") );
		dataSource.setInitialSize( Integer.parseInt(ps.getProperty("initialsize","2")) );
		dataSource.setMaxActive( Integer.parseInt(ps.getProperty("maxactive","20")));
		dataSource.setMaxIdle( Integer.parseInt(ps.getProperty("maxidle","0")));
		dataSource.setMaxWait( Long.parseLong(ps.getProperty("maxwait","0")));   
		
	}

}
