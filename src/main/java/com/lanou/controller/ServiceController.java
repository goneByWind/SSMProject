package com.lanou.controller;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.Account;
import com.lanou.bean.Service;
import com.lanou.service.ServiceService;
import com.lanou.utils.AjaxResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @RequestMapping(value = "/servicepage_detail")
    public String servicePageDetail(){
        return "service/service_detail";
    }

    @RequestMapping(value = "/servicepage_modi")
    public String servicePageModi(){
        return "service/service_modi";
    }

    // 模糊查询+显示全部
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
    // 添加service
    @ResponseBody
    @RequestMapping(value = "/addNewService")
    public AjaxResult addNewService(Service service){
        service.setStatus("0");
        service.setCreateDate(new Date());
        serviceService.addNew(service);
        return new AjaxResult("新增服务账号成功!");
    }

    // 暂停service账户
    @ResponseBody
    @RequestMapping(value = "/pauseService")
    public AjaxResult pauseAccount(@RequestParam("serviceId") Integer id){
        Service service = new Service();
        service.setServiceId(id);
        service.setPauseDate(new Date());
        service.setStatus("1");
        serviceService.updateService(service);
        return new AjaxResult("已暂停serviceId:"+id+"的业务账户!");
    }

    // 开通service账户
    @ResponseBody
    @RequestMapping(value = "/startService")
    public AjaxResult startAccount(@RequestParam("serviceId") Integer id) throws ParseException {

        /*设置service账号能够开通的条件:service账号上属的account账号处于开通状态,也就是非删除非暂停*/
        Service serviceByIdWithCascade = serviceService.findById(id);
        String accountStatus = serviceByIdWithCascade.getAccount().getStatus();
        if (accountStatus.equals("1")){
            Service service = new Service();
            service.setServiceId(id);
            // 开通时将暂停时间清空
            Date date = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            date=f.parse("0000-00-00");
            service.setPauseDate(date);

            service.setStatus("0");
            serviceService.updateService(service);

            return new AjaxResult("已开通serviceId:"+id+"的业务账户!");
        }else {
            return new AjaxResult("无法开通serviceId:"+id+"的业务账户,因为它的上级account账户处于删除或暂停状态");
        }
    }

    // 删除account账户,其实为标志位改为2,并且在前端页面不提供改,删方法
    @ResponseBody
    @RequestMapping(value = "/deleteService")
    public AjaxResult deleteAccount(@RequestParam("serviceId") Integer id) throws ParseException {

        Service service = new Service();
        service.setServiceId(id);
        // 删除时,顺便将暂停时间设置为0000-00-00
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        date=f.parse("0000-00-00");
        service.setPauseDate(date);
        service.setCloseDate(new Date());
        service.setStatus("2");
        serviceService.updateService(service);

        return new AjaxResult("已删除serviceId"+id+"业务账户!");
    }

    // 将id保存到session域中
    @ResponseBody
    @RequestMapping(value = "/saveIdToSessionForServiceModule")
    public void saveIdToSession(HttpServletRequest request,
                                @RequestParam("serviceId") Integer id){
        request.getSession().setAttribute("serviceIdSavedInSession",id);
    }

    // 显示详情
    @ResponseBody
    @RequestMapping(value = "/showDetailsForService")
    public AjaxResult showDetailsForService(HttpServletRequest request){
        Integer id = (Integer) request.getSession().getAttribute("serviceIdSavedInSession");
        Service serviceForShowDetails = serviceService.findById(id);
        return new AjaxResult(serviceForShowDetails,0,"显示点击的service的详细信息");
    }


}
