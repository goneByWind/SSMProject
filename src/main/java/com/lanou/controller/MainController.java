package com.lanou.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dllo on 17/10/20.
 */
@Controller
@Scope("prototype")
public class MainController {

    @RequestMapping("/home")
    public String homePage(){
        return "index";
    }

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }
    // 账单主页
    @RequestMapping(value = "/billpage_list")
    public String billPageList(){
        return "bill/bill_list";
    }
    // 报表主页
    @RequestMapping(value = "/reportpage_list")
    public String reportPageList(){
        return "report/report_list";
    }
    // 个人信息主页
    @RequestMapping(value = "/userpage_list")
    public String userPageList(){
        return "user/user_info";
    }
    // 修改密码页面
    @RequestMapping(value = "/userpage_modi_pwd")
    public String userPageModiPwd(){
        return "user/user_modi_pwd";
    }


}
