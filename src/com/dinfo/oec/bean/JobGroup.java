package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JobGroup implements Serializable{
	private String jobid;
	
	private List<JobItem> items = new ArrayList<JobItem>();

	
	
	public JobGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JobGroup(String jobid, List<JobItem> items) {
		super();
		this.jobid = jobid;
		this.items = items;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public List<JobItem> getItems() {
		return items;
	}

	public void setItems(List<JobItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "JobGroup [items=" + items + ", jobid=" + jobid + "]\r\n";
	}




	
	
	
}
