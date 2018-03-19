package com.webjava.utils;

/**
 * Created by Jerry on 2017/10/15.
 */
public class ResponseInfo {
    public ResponseInfo(int code,String msg){
        this.msg = msg;
        this.code = code;
        this.data = "";
    }

    public ResponseInfo(int code,String msg,String data){
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private int code;
    private String msg;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data;

}
