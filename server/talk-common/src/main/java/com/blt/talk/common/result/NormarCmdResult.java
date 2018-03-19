/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.result;

/**
 * 通用返回错误码
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public enum NormarCmdResult implements ResultEnum {

    SUCCESS(0, "成功"),
    FAILD(1, "失败"),
    PARAM_ERROR(2, "参数错误"),
    DB_ERROR(6, "DB异常"),
    DFS_ERROR(7, "文件存储服务异常");

    private int code;
    private String message;

    private NormarCmdResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.common.result.ResultEnum#getCode()
     */
    @Override
    public int getCode() {
        return this.code;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.common.result.ResultEnum#getMessage()
     */
    @Override
    public String getMessage() {
        return this.message;
    }

}
