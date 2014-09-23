package com.dinfo.oec.exception;

/**
 * 自定义异常
 * @author Administrator
 *
 */
public class OecException extends Exception{
    public OecException(){
    }
    public OecException(String Msg){
        super(Msg);
    }
}
