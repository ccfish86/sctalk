/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.result;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public enum GroupCmdResult implements ResultEnum {

    SUCCESS(0, "成功"),
    LIST_NORMAL_FAILD(1, "查询失败");

    private int code;
    private String message;

    private GroupCmdResult(int code, String message) {
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
