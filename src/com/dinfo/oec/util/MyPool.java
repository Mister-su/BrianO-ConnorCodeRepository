package com.dinfo.oec.util;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MyPool extends ThreadPoolExecutor{
    public boolean hasFinish = false;
    public MyPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, handler);
        // TODO Auto-generated constructor stub
    }

    public MyPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory);
        // TODO Auto-generated constructor stub
    }

    public MyPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        // TODO Auto-generated constructor stub
    }

    public MyPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        // TODO Auto-generated method stub
        super.afterExecute(r, t);
        synchronized(this){
            if(this.getActiveCount() == 1){
                this.hasFinish=true; 
                this.notify(); 
            }
       }
    }
    public void isEndTask(){
        synchronized (this) {
            while(this.hasFinish == false){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
}
