package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobItem implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jobid;
	//投诉时间。
	private String complainttime;
	//结束时间。
	private String endtime;
	
	private String dealtime;
	
	private String content;
	
	private ParseInfo parseInfo;
	
	
	/**
	 * 
	 * @param jobid   			工单的Jbod
	 * @param complainttime     完成时间
	 * @param endtime			结束时间
	 * @param dealtime			处理时间
	 * @param content			工单内容
	 */
	public JobItem(String jobid, String complainttime, String endtime,
			String dealtime, String content) {
		super();
		this.jobid = jobid;
		this.complainttime = complainttime;
		this.endtime = endtime;
		this.dealtime = dealtime;
		this.content = content;
	}

	public JobItem() {
		super();
		// TODO Auto-generated constructor stub
	}



	public JobItem(String jobid, String content) {
		super();
		this.jobid = jobid;
		this.content = content;
	}



	private List<String> catlogPath ;
	
	private List<String> weights = new ArrayList<String>();
	
	
	public List<String> getWeights() {
		return weights;
	}

	public void setWeights(List<String> weights) {
		this.weights = weights;
	}

	public ParseInfo getParseInfo() {
		return parseInfo;
	}

	public void setParseInfo(ParseInfo parseInfo) {
		this.parseInfo = parseInfo;
	}


	public List<String> getCatlogPath() {
		return catlogPath;
	}

	public void setCatlogPath(List<String> catlogPath) {
		this.catlogPath = catlogPath;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}




	public String getComplainttime() {
		return complainttime;
	}

	public void setComplainttime(String complainttime) {
		this.complainttime = complainttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getDealtime() {
		return dealtime;
	}

	public void setDealtime(String dealtime) {
		this.dealtime = dealtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "JobItem [catlogPath=" + catlogPath + ", complainttime="
				+ complainttime + ", content=" + content + ", dealtime="
				+ dealtime + ", endtime=" + endtime + ", jobid=" + jobid
				+ ", parseInfo=" + parseInfo + ", weights=" + weights + "]\r\n";
	}
	
	

}
