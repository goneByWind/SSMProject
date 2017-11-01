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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @ResponseBody
    @RequestMapping("/judageRealName")
    public boolean judageRealName(@RequestParam("realName")String realName){
        String rex = "^[a-zA-Z\\d\\u2E80-\\u9FFF]{1,20}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(realName);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
    @ResponseBody
    @RequestMapping("/judageIdCardNo")
    public boolean judageIdCardNo(@RequestParam("idcardNo")String idcardNo){
        String rex = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" +
                "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" +
                "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(idcardNo);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
    @ResponseBody
    @RequestMapping("/judgeLoginName")
    public boolean judgeLoginName(@RequestParam("loginName")String loginName){
        String rex = "^[a-zA-Z\\d\\_]{1,30}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(loginName);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
    @ResponseBody
    @RequestMapping("/judgeLoginPasswd")
    public boolean judgeLoginPasswd(@RequestParam("loginPasswd")String loginPasswd){
        String rex = "^[a-zA-Z\\d\\_]{1,30}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(loginPasswd);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
    @ResponseBody
    @RequestMapping("/judgeTelephone")
    public boolean judgeTelephone(@RequestParam("telephone")String telephone){
        String rex = "^1\\d{10}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(telephone);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
    @ResponseBody
    @RequestMapping("/judgeReferAccountID")
    public boolean judgeReferAccountID(@RequestParam("recommenderId")String recommenderId){
        String rex = "^1\\d{3}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(recommenderId);

        if (recommenderId==null||recommenderId.length()==0){
            return true;
        }

        if (m.find()){
            // 去数据库查询是否有这个推荐人Id号码:
            Account accountById = accountService.findAccountById(Integer.parseInt(recommenderId));
            // 如果有推荐人的id,说明是正确的
            if (accountById!=null){
                return true;
            }
        }

        return false;
    }

    @ResponseBody
    @RequestMapping("/judgeAccountEmail")
    public boolean judgeAccountEmail(@RequestParam(value = "email",required = false) String email){
        String rex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(email);

        if (email==null||email.length()==0){
            return true;
        }

        if (m.find()){
            return true;
        }

        return false;
    }

    @ResponseBody
    @RequestMapping("/judgeAccountAddress")
    public boolean judgeAccountAddress(@RequestParam(value = "mailaddress",required = false) String mailaddress){
        String rex = "^[a-zA-Z\\d\\u2E80-\\u9FFF]{0,50}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(mailaddress);

        if (m.find()){
            return true;
        }
        return false;
    }
    @ResponseBody
    @RequestMapping("/judgeZipcode")
    public boolean judgeZipcode(@RequestParam(value = "zipcode",required = false) String zipcode){
        String rex = "^\\d{6}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(zipcode);

        if (zipcode==null||zipcode.length()==0){
            return true;
        }
        if (m.find()){
            return true;
        }
        return false;
    }
    @ResponseBody
    @RequestMapping("/judgeQQ")
    public boolean judgeQQ(@RequestParam(value = "qq",required = false) String qq){
        String rex = "^\\d{5,13}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(qq);

        if (qq==null||qq.length()==0){
            return true;
        }
        if (m.find()){
            return true;
        }
        return false;
    }
}
