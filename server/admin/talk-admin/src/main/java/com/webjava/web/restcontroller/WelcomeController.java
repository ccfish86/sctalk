/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.webjava.web.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Controller
public class WelcomeController {

    @RequestMapping("/")
    public String index() {
        return "forward:/admin/index.html";
    }
    @RequestMapping("/admin")
    public String hindex() {
        return "forward:/admin/index.html";
    }
}
