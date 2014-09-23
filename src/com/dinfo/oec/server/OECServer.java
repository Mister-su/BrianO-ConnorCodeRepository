package com.dinfo.oec.server;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.dinfo.init.DataBaseConfig;
import com.dinfo.init.DbConfig;
import com.dinfo.init.ResourceInit;

public class OECServer {
	
	private static OECServer minaServer = null;
	//创建一个非阻塞的Server端Socket
	private SocketAcceptor acceptor = new NioSocketAcceptor();
	//创建接收数据的过滤器
	private DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();        
	private int bindPort = DbConfig.serverport;
	//单例
	public static OECServer getInstances() {  
		if (null == minaServer) {               
			minaServer = new OECServer();    
		}            
		return minaServer;    
	}
	
	private OECServer() { 
        // 协议解析，采用mina现成的UTF-8字符串处理方式
		chain.addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        // 设置消息处理类（创建、关闭Session，可读可写等等，继承自接口IoHandler）
		acceptor.setHandler(new OECHandler());
        // 设置接收缓存区大小
		//acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		try {
            // 服务器开始监听
			acceptor.bind( new InetSocketAddress(bindPort) );
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceInit.init();
		DataBaseConfig.init();
		OECServer.getInstances();
		System.out.println("服务开启8888.。。。");
	}

}
