package com.lanou.controller;

import com.lanou.bean.ModuleInfo;
import com.lanou.service.ModuleInfoService;
import com.lanou.utils.AjaxResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dllo on 17/10/26.
 */
@Controller
@Scope("prototype")
public class ModuleInfoController {

    @Resource
    private ModuleInfoService moduleInfoService;

    // 查找全部权限
    @ResponseBody
    @RequestMapping(value = "/showAllModules")
    public AjaxResult showAllModules(){
        List<ModuleInfo> moduleInfoList = moduleInfoService.findAll();
        return new AjaxResult(moduleInfoList,0,"返回全部的权限");
    }
}
