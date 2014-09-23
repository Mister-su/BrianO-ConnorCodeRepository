package com.dinfo.oec.server;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.PacketBean;
import com.dinfo.oec.bean.ParseInfo;
import com.dinfo.oec.main.OECParseMain;


public class OECHandler extends IoHandlerAdapter{
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)throws Exception {
		cause.printStackTrace();
		System.out.println("客户端主动断开连接");
	}

    /*
     * 这个方法是目前这个类里最主要的，
     * 当接收到消息，只要不是quit，就把服务器当前的时间返回给客户端
     */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
//		System.out.println(message);
//		PacketBean packet = (PacketBean)message;
//		PacketBean res = new PacketBean();
//		OECParseMain sf = new OECParseMain();
//		int type = packet.getType();
//		if(type==0){
//			JobItem itme = packet.getItem();
////			ParseInfo info = sf.parseItem(itme);
//			res.setInfo(info);
//			res.setType(0);
//		}else if(type==1){
//			List<JobItem> items = packet.getItems();
//			ParseInfo info = sf.parseItemList(items);
//			res.setInfo(info);
//			res.setType(1);
//		}else if(type==2){
//			sf.reloadResource();
//			res.setType(2);
//		}else{
//			res.setType(-1);
//		}
//		String address = ((InetSocketAddress)session.getRemoteAddress()).getHostName();
//		int port = ((InetSocketAddress)session.getRemoteAddress()).getPort();
//		System.out.println("客户端连接:"+address+":"+port);
//		session.write(res);//返回个客户端
		
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		System.out.println("客户端与服务端断开连接.....");
	}
	

}
