/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.model;

import com.blt.talk.common.result.ResultEnum;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * 各消息/服务间用通用Model
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class BaseModel<T> {

    /** status code */
    private Integer code = 0;
    
    /** status message(while code != 0) */
    private String msg;

    /** return data */
    private T data;

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the data
     */
    @JsonUnwrapped
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    @JsonUnwrapped
    public void setData(T data) {
        this.data = data;
    }
    
    /**
     * Set then execute result with a enum
     * @param result then result enum to set
     * @return a reference to this object.
     * @since  1.0
     */
    public BaseModel<T> setResult(ResultEnum result) {
        this.code = result.getCode();
        this.msg = result.getMessage();
        return this;
    }

}
