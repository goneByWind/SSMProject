package com.lanou.controller;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.RoleInfo;
import com.lanou.service.RoleInfoService;
import com.lanou.utils.AjaxResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by dllo on 17/10/26.
 */
@Controller
@Scope("prototype")
public class RoleInfoController {

    @Resource
    private RoleInfoService roleInfoService;

    @RequestMapping("/rolepage_list")
    public String rolePageList(){
        return "/role/role_list";
    }

    @RequestMapping("/rolepage_modi")
    public String rolePageModi(){
        return "role/role_modi";
    }

    @RequestMapping("/rolepage_add")
    public String rolePageAdd(){
        return "role/role_add";
    }

    // 分页+显示功能
    @ResponseBody
    @RequestMapping("/showAllMessagesWithSeparatePages")
    public AjaxResult showAllMessagesWithSeparatePages(@RequestParam("pageNo")Integer pageNo,
                                                       @RequestParam("pageSize")Integer pageSize){

        PageInfo<RoleInfo> roleInfoPageInfo = roleInfoService.roleInfoPageInfo(pageNo, pageSize);
        return new AjaxResult(roleInfoPageInfo,0,"返回分页后的显示全部(包含级联查询的信息)");
    }

    // 将点击"修改"按钮所关联的roleId保存到session域,为修改功能做准备---"修改谁"
    @ResponseBody
    @RequestMapping("/saveIdToSessionForRoleInfo")
    public void  saveIdToSessionForRoleInfo(HttpServletRequest request,
                                            @RequestParam("roleId") Integer roleId){
        request.getSession().setAttribute("roleIdSavedInSession",roleId);
    }
    // 已经进入在修改页面之后,在session域中得到相应的roleId
    // 然后通过id在数据库中查找相应的roleInfo对象
    @ResponseBody
    @RequestMapping("/getRoleIdInSession")
    public AjaxResult getRoleIdInSession(HttpServletRequest request){
        Integer id = (Integer) request.getSession().getAttribute("roleIdSavedInSession");
        RoleInfo roleInfo = roleInfoService.findRoleInfoById(id);
        return new AjaxResult(roleInfo,0,"返回roleInfo对象(包含moduleInfoList)");
    }
    // 修改功能的实现
    @ResponseBody
    @RequestMapping("/modifyRoleInfo")
    public AjaxResult modifyRoleInfo(HttpServletRequest request,
                                     @RequestParam("name") String roleName,
                                     @RequestParam("moduleInfoList") String moduleInfos){
        System.out.println(roleName);
        System.out.println(moduleInfos);
        // 提取出moduleId(前端传回的数据清洗)[准备阶段]
        for (int i=0;i<moduleInfos.length()/2;i++){
            Character c = moduleInfos.charAt(i*2 + 1);
            Integer numericValue = Character.getNumericValue(c);
            System.out.println(numericValue);
        }

        // 设置要更改的role对象
        Integer roleId = (Integer) request.getSession().getAttribute("roleIdSavedInSession");
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleId(roleId);
        roleInfo.setName(roleName);

        // 更新role的名字
        roleInfoService.updateRole(roleInfo);
        // 删除原role_id的所有的中间表role_module条目
        roleInfoService.deleteRoleModules(roleId);
        // 重新逐条插入中间表的数据
        for (int i=0;i<moduleInfos.length()/2;i++){
            Character c = moduleInfos.charAt(i*2 + 1);
            Integer moduleId = Character.getNumericValue(c);
            roleInfoService.saveRoleModules(roleId,moduleId);

        }
        return new AjaxResult("修改完成");
    }

    // 添加功能的实现
    /*
    由于报错
    HTTP Status 400 - Required String parameter 'moduleInfoList' is not present
    改正方法
    @RequestParam(value = "name",required=false) String roleName,
    @RequestParam(value = "moduleInfoList",required = false) String moduleInfos
    ??前面的修改方法写的一样,为什么就不报错误呢
    */
    @ResponseBody
    @RequestMapping("/addRoleInfo")
    public AjaxResult addRoleInfo(@RequestParam(value = "name",required=false) String roleName,
                                  @RequestParam(value = "moduleInfoList",required = false) String moduleInfos){
        System.out.println(roleName);
        System.out.println(moduleInfos);
        // 设置要添加的role对象
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(roleName);
        // 添加role对象
        roleInfoService.addRole(roleInfo);
        // 通过roleName查找roleId
        RoleInfo role = roleInfoService.findRoleByRoleName(roleName);
        Integer roleId = role.getRoleId();
        // 逐条插入中间表的数据
        for (int i=0;i<moduleInfos.length()/2;i++){
            Character c = moduleInfos.charAt(i*2 + 1);
            Integer moduleId = Character.getNumericValue(c);
            roleInfoService.saveRoleModules(roleId,moduleId);
        }
        return new AjaxResult("添加完成");
    }
    // 删除功能
    @ResponseBody
    @RequestMapping(value = "/deleteRoleInfo")
    public AjaxResult deleteRoleInfo(@RequestParam(value = "roleId") Integer roleId){
        roleInfoService.deleteRoleById(roleId);
        roleInfoService.deleteRoleModules(roleId);
        return new AjaxResult("成功删除roleInfo对象以及它的所有中间表对象");
    }
}
