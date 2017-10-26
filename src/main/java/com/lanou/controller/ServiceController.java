package com.lanou.controller;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.Service;
import com.lanou.service.ServiceService;
import com.lanou.utils.AjaxResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by dllo on 17/10/25.
 */
@Controller
@Scope("prototype")
public class ServiceController {

    @Resource
    private ServiceService serviceService;

    @RequestMapping(value = "/servicepage_list")
    public String servicePageList(){
        return "service/service_list";
    }
    @RequestMapping(value = "/servicepage_add")
    public String servicePageAdd(){
        return "service/service_add";
    }

    @ResponseBody
    @RequestMapping(value = "/searchServicesInServicePage")
    public AjaxResult searchServicesInServicePage(@RequestParam(value = "osUsername",required=false) String osUsername,
                                                  @RequestParam(value = "unixHost",required=false) String unixHost,
                                                  @RequestParam(value = "idcardNo",required=false) String idcardNo,
                                                  @RequestParam(value = "status",required=false) String status,
                                                  @RequestParam(value = "no",required=false) Integer pageNo,
                                                  @RequestParam(value = "size",required=false) Integer pageSize){


        PageInfo<Service> servicePageInfo = serviceService.selectByInfo(osUsername, unixHost, idcardNo, status, pageNo, pageSize);

        return new AjaxResult(servicePageInfo,0,"模糊查询及显示全部");

    }
    @ResponseBody
    @RequestMapping(value = "/addNewService")
    public AjaxResult addNewService(Service service){
        service.setStatus("0");
        service.setCreateDate(new Date());
        serviceService.addNew(service);
        return new AjaxResult("新增服务账号成功!");
    }

}
