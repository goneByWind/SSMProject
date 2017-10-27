package com.lanou.service;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.RoleInfo;

/**
 * Created by dllo on 17/10/26.
 */
public interface RoleInfoService {
    // 返回分页信息用于显示全部
    PageInfo<RoleInfo> roleInfoPageInfo(Integer pageNo, Integer pageSize);

    // 通过id查找
    RoleInfo findRoleInfoById(Integer id);

    // 更改role的名字
    void updateRole(RoleInfo roleInfo);

    // 删除原role_id的所有的中间表role_module条目
    void deleteRoleModules(Integer id);

    // 保存 修改后的中间表信息到数据库中的中间表role_module
    void saveRoleModules(Integer roleId, Integer moduleId);

    // 添加role对象
    void addRole(RoleInfo roleInfo);

    // 通过roleName查找roleId
    RoleInfo findRoleByRoleName(String roleName);

    // 用过id删除Role对象
    void deleteRoleById(Integer roleId);
}
