package com.lanou.mapper;

import com.lanou.bean.RoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleInfoMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(RoleInfo record);

    int insertSelective(RoleInfo record);

    RoleInfo selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(RoleInfo record);

    int updateByPrimaryKey(RoleInfo record);

    List<RoleInfo> findRoleInfoListByModuleId(@Param("moduleId") Integer moduleId);

    List<RoleInfo> findRoleInfoListByAdminId(@Param("adminId") Integer adminId);

    List<RoleInfo> findAll();

    RoleInfo findRoleInfoById(@Param("roleId") Integer id);

    /*以下两个方法都是对中间表的修改,能够运行成功,
      但是感觉在roleMap中直接改中间表role_module不规范
      注意:insertRoleModule的sql我只写了一个parameterType="java.lang.Integer"也可以实现功能,
      我还以为得建一个role_module的实体类:parameterType="com.lanou.bean.role_module"才可以,
      事实证明不用
    */

    void deleteRoleModules(@Param("roleId") Integer id);

    void insertRoleModule(@Param("roleId") Integer roleId,
                          @Param("moduleId") Integer moduleId);

    RoleInfo findRoleByRoleName(@Param("roleName") String roleName);
}