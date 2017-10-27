package com.lanou.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dllo on 17/10/27.
 */
@Controller
@Scope("prototype")
public class AdminInfoController {
    @RequestMapping(value = "/adminpage_list")
    public String adminPageList(){
        return "admin/admin_list";
    }
    @RequestMapping(value = "/adminpage_add")
    public String adminPageAdd(){
        return "admin/admin_add";
    }
    @RequestMapping(value = "adminpage_modi")
    public String adminPageModi(){
        return "admin/admin_modi";
    }
}
