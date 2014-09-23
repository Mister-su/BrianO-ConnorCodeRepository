package com.dinfo.oec.client.net;

import java.util.ArrayList;
import java.util.List;

import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.ParseInfo;

public class DriverManager {
    private String ip;
    private int port;
    /**
     * 获取服务端连接
     * @param ip
     * @param port
     * @return
     */
    public Connection getConnetion(String ip,int port){
        this.ip = ip;
        this.port = port;
        return new Connection(this.ip,this.port).getInstanse();
    }
    public static void main(String[] args) {
    	List<JobItem> items = new ArrayList<JobItem>();
    	for(int i=1; i<=10000; i++){
    		JobItem job = new JobItem("","","","","海尔维修人员服务态度很好呀");
    		items.add(job);
    	}
        Connection conn = new DriverManager().getConnetion("192.168.1.167", 8888);
        ParseInfo info = conn.sentMessage(items);
        System.out.println("");
    }
}
