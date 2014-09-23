package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.List;

public class PacketBean implements Serializable{
    private int type;   //0,单条记录;1,多条记录；2,加载资源；-1,失败；
    private JobItem item;
    private List<JobItem> items;
    private ParseInfo info;
    public PacketBean(){
        
    }
    public PacketBean(int type) {
        // TODO Auto-generated constructor stub
        this.type = type;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public JobItem getItem() {
        return item;
    }
    public void setItem(JobItem item) {
        this.item = item;
    }
    public List<JobItem> getItems() {
        return items;
    }
    public void setItems(List<JobItem> items) {
        this.items = items;
    }
    public ParseInfo getInfo() {
        return info;
    }
    public void setInfo(ParseInfo info) {
        this.info = info;
    }
    
}
