package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;

public class DozerMapper implements Serializable{

	private static DozerBeanMapper mapper;
	static {
		mapper = new DozerBeanMapper();
		List<String> xmls = new ArrayList<String>();
		xmls.add("com/dinfo/crawl/bean/BeanMapper.xml");
		mapper.setMappingFiles(xmls);
	}


}
