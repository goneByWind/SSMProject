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

    @RequestMapping("/homepage")
    public String homePage(){
        return "fee/fee_list";
    }
}
