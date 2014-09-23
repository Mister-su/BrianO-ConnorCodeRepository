package com.dinfo.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//用于功能控制板。
public class ControlConfig {
	private static  boolean enabBestOntology;
	public static ThreadLocal<Long> local = new ThreadLocal<Long>();
	public static void init(){
		InputStream in  = DataBaseConfig.class.getClassLoader().getResourceAsStream("panel.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		enabBestOntology = "true".equals(pro.getProperty("enabBestOntology", "true"));
		System.out.println(enabBestOntology);
	}

	public static boolean isEnabBestOntology() {
		return enabBestOntology;
	}
}
