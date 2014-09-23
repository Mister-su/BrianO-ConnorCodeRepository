package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.List;

public class JobInfo implements Serializable{

	String speed;
	List<JobItem> jobList;
	String elapsed;
	
	public String getElapsed() {
		return elapsed;
	}
	public void setElapsed(String elapsed) {
		this.elapsed = elapsed;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public List<JobItem> getJobList() {
		return jobList;
	}
	public void setJobList(List<JobItem> jobList) {
		this.jobList = jobList;
	}
	@Override
	public String toString() {
		return "JobInfo [elapsed=" + elapsed + ", jobList=" + jobList
				+ ", speed=" + speed + "]\r\n";
	}
	
	
} 
