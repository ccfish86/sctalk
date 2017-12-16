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
public enum LoginCmdResult implements ResultEnum {

    SUCCESS(0, "成功"),
    LOGIN_NOUSER(1, "用户名/密码错误"),
    LOGIN_WRONG_PASSWORD(2, "密码错误"),
    REGIST_EXISTED_USER(3, "用户名已被注册"),
    LOGIN_PASSWORD_LOCK(6, "用户名/密码错误次数太多");

    private int code;
    private String message;

    private LoginCmdResult(int code, String message) {
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
