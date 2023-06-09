/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.account;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.param.RegistReq;
import com.blt.talk.common.util.SecurityUtils;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class AccountSampleTest {

    @Test
    public void test() {
        
        RestTemplate rt = new RestTemplate();
        
        for (int i = 10; i< 100;i++) {
            System.out.println(i%2);
            RegistReq req = new RegistReq();
            req.setName("" + i);
            req.setSex(i%2);
            req.setPhone("0000" + i);
            req.setPassword(SecurityUtils.getInstance().EncryptPass("" + i));
            req.setEmail(i + "@hifipi.com");
            BaseModel res = rt.postForObject("http://tt.hifipi.com/regist", req, BaseModel.class);
            System.out.println(res.getCode());
        }
        
    }
}
