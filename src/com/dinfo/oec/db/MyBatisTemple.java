package com.dinfo.oec.db;


import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dinfo.init.DbConfig;

public class MyBatisTemple  {


	public  Object Execute(Class clzz , String method ,Object ...arg ) 
	{
		SqlSessionFactory factory = DbConfig.factory;
		SqlSession session = factory.openSession(true);
		Object mapper = session.getMapper(clzz);
		Object result;
		try {
			result = MethodUtils.invokeMethod(mapper, method, arg);
			session.close();
			return result;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
