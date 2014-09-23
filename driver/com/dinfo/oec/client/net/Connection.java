package com.dinfo.oec.client.net;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.PacketBean;
import com.dinfo.oec.bean.ParseInfo;
import com.dinfo.oec.client.Test;
import com.dinfo.oec.client.hanlder.MinaClientHanlder;
import com.dinfo.oec.exception.OecException;


public class Connection {
    private String ip;
    private int port;
    private ConnectFuture cf;
    private NioSocketConnector connector;
    private IoSession session;
    private MinaClientHanlder hanlder = new MinaClientHanlder();
    public Connection(String ip, int port) {
        // TODO Auto-generated constructor stub
        this.ip = ip;
        this.port = port;
    }
    /**
     * 返回 服务端连接
     * @return
     */
    public Connection getInstanse() {
        // TODO Auto-generated method stub
        connector=new NioSocketConnector();  
        // 获取过滤器链           
        connector.getFilterChain().addLast( "logger", new LoggingFilter() ); 
        connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory())); 
       // 消息核心处理器        
        connector.setHandler(hanlder);  
       // 设置链接超时时间        
        connector.setConnectTimeoutCheckInterval(30000);  
        //读写同步
        SocketSessionConfig  cfg = connector.getSessionConfig();
        cfg.setUseReadOperation(true);
       // 连接服务器，知道端口、地址       
        cf = connector.connect(new InetSocketAddress(ip,port));  
       // 等待连接创建完成       
        cf.awaitUninterruptibly();  
        session = cf.getSession();
        return this;
    }
    /**
     * 关闭连接
     */
    public void close(){
        session.close(true);
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }
    /**
     * 
     */
    public void sentMessage(){
       try {
           Test t = new Test();
           t.setId(1);
           session.write(t);
           ReadFuture readFuture = session.read().awaitUninterruptibly();
           System.out.println(readFuture.getMessage().toString()+"2222");
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            close();
        }
       
    }
    /**
     * 发送单条记录
     * @param item
     */
    public ParseInfo sentMessage(JobItem item){
        PacketBean bean = null;
        try {
            bean = new PacketBean(0);
            bean.setItem(item);
            session.write(bean);
            ReadFuture readFuture = session.read().awaitUninterruptibly();
            bean = (PacketBean) readFuture.getMessage();
            if(bean.getType() == -1){
                throw new OecException("处理 单条 记录失败");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            close();
        }
        return bean.getInfo();
    }
    /**
     * 发送多条记录
     * @param items
     * @return
     */
    public ParseInfo sentMessage(List<JobItem> items){
        PacketBean bean = null;
        try {
            bean = new PacketBean(1);
            bean.setItems(items);
            session.write(bean);
            ReadFuture readFuture = session.read().awaitUninterruptibly();
            bean = (PacketBean) readFuture.getMessage();
            if(bean.getType() == -1){
                throw new OecException("处理 多条记录失败");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            close();
        }
        return bean.getInfo();
    }
    /**
     * 重新加载资源
     */
    public boolean reload(){
        PacketBean bean = null;
        try {
            bean = new PacketBean(2);
            session.write(bean);
            ReadFuture readFuture = session.read().awaitUninterruptibly();
            bean = (PacketBean) readFuture.getMessage();
            if(bean.getType() == -1){
                throw new OecException("处理 重新加载资源失败");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            close();
        }
        return true;
    }
}
