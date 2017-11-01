package com.lanou.service;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.AdminInfo;

/**
 * Created by dllo on 17/10/28.
 */
public interface AdminInfoService {
    // 显示全部及分页
    PageInfo<AdminInfo> adminPageInfo(Integer pageNo, Integer pageSize,Integer moduleId,String roleName);

    // 添加新的admin
    void addNewAdmin(AdminInfo adminInfo);

    // 通过adminCode(登录名)名字查找adminId
    AdminInfo findByAdminCode(String adminCode);

    // 保存中间表admin_role
    void saveAdminRole(Integer adminId, Integer roleId);

    // 通过id查找
    AdminInfo findAdminByIdWithCascade(Integer adminId);

    // 修改
    void updateAdmin(AdminInfo adminInfo);

    // 根据adminId删除中间表admin_role条目
    void deleteAdminRoles(Integer adminId);

    // 通过id删除
    void deleteAdminInfoById(Integer adminId);

    AdminInfo findAdminByName(String adminName);

    AdminInfo findAdminByAdminCode(String adminCode);
}
