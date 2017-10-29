package com.lanou.controller;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.AdminInfo;
import com.lanou.service.AdminInfoService;
import com.lanou.utils.AjaxResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by dllo on 17/10/27.
 */
@Controller
@Scope("prototype")
public class AdminInfoController {
    @Resource
    private AdminInfoService adminInfoService;
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

    // 分页+显示全部+模糊查询
    @ResponseBody
    @RequestMapping(value = "/showAllMessagesWithSeparatePagesForAdmin")
    public AjaxResult showAllMessagesWithSeparatePagesForAdmin(@RequestParam(value = "pageNo",required = false) Integer pageNo,
                                                               @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                                               @RequestParam(value = "moduleId",required = false) Integer moduleId,
                                                               @RequestParam(value = "roleName",required = false) String roleName){
        PageInfo<AdminInfo> adminInfoPageInfo = adminInfoService.adminPageInfo(pageNo, pageSize,moduleId,roleName);
        return new AjaxResult(adminInfoPageInfo,0,"显示全部+分页功能+模糊查询");
    }
    // 添加
    @ResponseBody
    @RequestMapping(value = "/addNewAdminInfo")
    public AjaxResult addNewAdminInfo(@RequestParam(value = "textForRoleList",required = false) String textForRoleList,
                                      AdminInfo adminInfo){
        System.out.println(textForRoleList);
        System.out.println(adminInfo);
        // 添加
        adminInfo.setEnrolldate(new Date());
        adminInfoService.addNewAdmin(adminInfo);
        // 通过adminCode(登录名)名字查找adminId
        AdminInfo byAdminCode = adminInfoService.findByAdminCode(adminInfo.getAdminCode());
        Integer adminId = byAdminCode.getAdminId();
        // 逐条插入中间表admin_role信息(对textForRoleList数据进行清洗)
        for (int i=0;i<textForRoleList.length()/4;i++){
            // 对字符串中的数字进行截取和转换
            String substring = textForRoleList.substring(i * 4 + 1, (i + 1) * 4);
            Integer roleId = Integer.parseInt(substring);
            System.out.println("roleId号:"+roleId);
            adminInfoService.saveAdminRole(adminId,roleId);
        }
        return new AjaxResult("添加新的管理员admin,并且完成级联关系添加");
    }

    // 修改前保存要修改的id到session
    @ResponseBody
    @RequestMapping(value = "/saveIdToSessionForAdminInfo")
    public void saveIdToSessionForAdminInfo(HttpServletRequest request,
                                            @RequestParam(value = "adminId")Integer adminId){
        request.getSession().setAttribute("adminIdSavedInSession",adminId);
    }
    // 修改前回显
    @ResponseBody
    @RequestMapping(value = "/getAdminIdInSession")
    public AjaxResult getAdminIdInSession(HttpServletRequest request){
        Integer adminId = (Integer) request.getSession().getAttribute("adminIdSavedInSession");
        AdminInfo admin = adminInfoService.findAdminByIdWithCascade(adminId);
        return new AjaxResult(admin,0,"传回用于回显的所有信息");
    }

    // 修改
    @ResponseBody
    @RequestMapping(value = "/modifyAdmin")
    public AjaxResult modiftAdminInfo(HttpServletRequest request,
                                      @RequestParam(value = "textForRoleList",required = false) String textForRoleList,
                                      AdminInfo adminInfo){
        System.out.println(textForRoleList);
        System.out.println(adminInfo);

        Integer adminId = (Integer) request.getSession().getAttribute("adminIdSavedInSession");
        adminInfo.setAdminId(adminId);

        // 修改admin对象
        adminInfoService.updateAdmin(adminInfo);
        // 删除这个admin对象所关联的所有中间表admin_role的条目
        adminInfoService.deleteAdminRoles(adminId);
        // 重新逐条插入中间表信息
        for (int i=0;i<textForRoleList.length()/4;i++){
            // 对字符串中的数字进行截取和转换
            String substring = textForRoleList.substring(i * 4 + 1, (i + 1) * 4);
            Integer roleId = Integer.parseInt(substring);
            System.out.println("roleId号:"+roleId);
            adminInfoService.saveAdminRole(adminId,roleId);
        }
        return new AjaxResult("修改完成");
    }
    // 删除
    @ResponseBody
    @RequestMapping(value = "/deleteAdminInfo")
    public AjaxResult deleteAdminInfo(@RequestParam(value = "adminId")Integer adminId){

        // 通过id删除admin_info表中的条目
        adminInfoService.deleteAdminInfoById(adminId);
        // 通过id删除admin_role中间表中的所有条目
        adminInfoService.deleteAdminRoles(adminId);
        return new AjaxResult("adminId为:"+adminId+"的管理员信息(包括admin_role中间表的信息)删除成功!");
    }
}
