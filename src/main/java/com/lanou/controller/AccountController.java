package com.lanou.controller;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.Account;
import com.lanou.bean.Service;
import com.lanou.service.AccountService;
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
import java.util.List;

/**
 * Created by dllo on 17/10/23.
 */
@Controller
@Scope("prototype")
public class AccountController {

    @Resource
    private AccountService accountService;

    @Resource
    private ServiceService serviceService;

    @RequestMapping(value = "/accountpage_list")
    public String accountPageList(){
        return "account/account_list";
    }

    @RequestMapping(value = "/accountpage_add")
    public String accountPageAdd(){
        return "account/account_add";
    }
    @RequestMapping(value = "/accountpage_detail")
    public String accountPageDetail(){
        return "account/account_detail";
    }
    @RequestMapping(value = "/accountpage_midi")
    public String accountPageModi(){
        return "account/account_modi";
    }

    // 返回account页面的分页信息
    @ResponseBody
    @RequestMapping(value = "/accountpageinfo")
    public AjaxResult accountPageInfo(@RequestParam("no") Integer pageNo,
                                      @RequestParam("size") Integer pageSize){
        PageInfo<Account> accountPageInfo = accountService.AccountPageinfo(pageNo, pageSize);

        return new AjaxResult(accountPageInfo,0,"返回分页工具");
    }

    // 添加account账户
    @ResponseBody
    @RequestMapping(value = "/addNewAccount")
    public AjaxResult addNewAccount(Account account) throws ParseException {

        account.setStatus("1");
        account.setCreateDate(new Date());

        // 通过身份证设置出生日期
        String idcardNo = account.getIdcardNo();
        String birthDate = idcardNo.substring(6, 10)+"-"+idcardNo.substring(10, 12)+"-"+idcardNo.substring(12, 14)+" 00:00:00";
        SimpleDateFormat sdf =new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = sdf.parse(birthDate);
        account.setBirthdate(date);

        System.out.println(account);

        /*验证身份证是否已经被注册*/
        Account accountByIdCard = accountService.findAccountByIdCard(idcardNo);
        // 数据库里以及存在这个身份证号码注册的账号了,不被允许再注册
        if (accountByIdCard!=null){
         return new AjaxResult(0,"身份证已经被注册!");
        }

        accountService.addNew(account);

        return new AjaxResult(1,"添加新的账号成功!");
    }

    // 暂停account账户
    /*同时暂停该account账号下属的所有service业务账号*/
    @ResponseBody
    @RequestMapping(value = "/pauseAccount")
    public AjaxResult pauseAccount(@RequestParam("accountId") Integer id){
        Account account = new Account();
        account.setAccountId(id);
        account.setPauseDate(new Date());
        account.setStatus("0");
        accountService.updateAccount(account);

        /*实现暂停下属的所有service业务账号*/
        Account accountByIdWithCascade = accountService.findAccountByIdWithCascade(id);
        List<Service> serviceList = accountByIdWithCascade.getServiceList();
        for (Service service : serviceList) {
            service.setPauseDate(new Date());
            service.setStatus("1");
            serviceService.updateService(service);
        }
        return new AjaxResult("已暂停accountId:"+id+"的账户!");
    }

    // 开通account账户
    @ResponseBody
    @RequestMapping(value = "/startAccount")
    public AjaxResult startAccount(@RequestParam("accountId") Integer id) throws ParseException {
        Account account = new Account();
        account.setAccountId(id);
        // 开通后删除暂停时间
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        date=f.parse("0000-00-00");
        account.setPauseDate(date);

        account.setStatus("1");
        accountService.updateAccount(account);
        return new AjaxResult("已开通accountId:"+id+"的账户!");
    }

    // 删除account账户,其实为标志位改为2,并且在前端页面不提供改,删方法
    /*删除account账户时,要级联删除其下属的所有service账号*/
    @ResponseBody
    @RequestMapping(value = "/deleteAccount")
    public AjaxResult deleteAccount(@RequestParam("accountId") Integer id) throws ParseException {
        Account account = new Account();
        account.setAccountId(id);
        // 删除时,顺便将暂停时间设置为0000-00-00
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        date=f.parse("0000-00-00");
        account.setPauseDate(date);
        account.setCloseDate(new Date());
        account.setStatus("2");
        accountService.updateAccount(account);
        /*实现暂停下属的所有service业务账号*/
        Account accountByIdWithCascade = accountService.findAccountByIdWithCascade(id);
        List<Service> serviceList = accountByIdWithCascade.getServiceList();
        for (Service service : serviceList) {
            // 删除时,顺便将暂停时间设置为0000-00-00
            service.setPauseDate(date);
            service.setCloseDate(new Date());
            service.setStatus("2");
            serviceService.updateService(service);
        }


        return new AjaxResult("已删除accountId:"+id+"的账户!");
    }

    // 将id保存到session域中
    @ResponseBody
    @RequestMapping(value = "/saveIdToSession")
    public void saveIdToSession(HttpServletRequest request,
                                @RequestParam("accountId") Integer id){
        request.getSession().setAttribute("accountIdSavedInSession",id);
    }

    // 显示详情
    @ResponseBody
    @RequestMapping(value = "/showAccountDetailsByIdInSession")
    public AjaxResult showAccountDetailsByIdInSession(HttpServletRequest request){
        Integer accountId = (Integer) request.getSession().getAttribute("accountIdSavedInSession");
        Account accountById = accountService.findAccountById(accountId);
        // 显示细节中推荐人的身份证号码:也要带到页面中并且显示出来↓
        Integer recommenderId = accountById.getRecommenderId();
        if(recommenderId!=null&&!recommenderId.equals("")){
            Account accountRecommender = accountService.findAccountById(recommenderId);
            String idcardNoOfRecommender = accountRecommender.getIdcardNo();
            return new AjaxResult(accountById,0,idcardNoOfRecommender);
        }
        return new AjaxResult(accountById,0,"  ");
    }

    @ResponseBody
    @RequestMapping(value = "/modifyAccount")
    public AjaxResult modifyAccount(Account account){
        System.out.println(account);
        accountService.updateAccount(account);
        return new AjaxResult("修改账号信息成功!");
    }

    @ResponseBody
    @RequestMapping(value = "/searchAccountsInAccountPage")
    public AjaxResult searchAccountsInAccountPage(@RequestParam("idcardNo") String idcardNo,
                                                  @RequestParam("realName") String realName,
                                                  @RequestParam("loginName") String loginName,
                                                  @RequestParam("status") String status,
                                                  @RequestParam("no") Integer pageNo,
                                                  @RequestParam("size") Integer pageSize){

        System.out.println(idcardNo);
        System.out.println(realName);
        System.out.println(loginName);
        System.out.println(status);

        PageInfo<Account> accountPageInfo = accountService.selectByInfo(idcardNo, realName, loginName, status, pageNo, pageSize);

        return new AjaxResult(accountPageInfo,0,"返回模糊查询的结果,并按模糊查询的结果进行分页");
    }

    @ResponseBody
    @RequestMapping("/searchAccountByIdcardNo")
    public AjaxResult searchAccountByIdcardNo(@RequestParam("idcardNo") String idcardNo){

        Account accountByIdCard = accountService.findAccountByIdCard(idcardNo);
        System.out.println(accountByIdCard);
        if (accountByIdCard!=null){
            return new AjaxResult(accountByIdCard,1,"账务账号存在~");
        }
        return new AjaxResult(0);
    }
    @ResponseBody
    @RequestMapping("/searchLoginName")
    public AjaxResult searchLoginName(@RequestParam("loginName")String loginName){
        Account accountByLoginName = accountService.findAccountByLoginName(loginName);
        if (accountByLoginName!=null){
            return new AjaxResult(accountByLoginName,1,"返回账务账号");
        }
        return new AjaxResult(0);
    }
}
