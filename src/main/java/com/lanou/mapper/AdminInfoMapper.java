package com.lanou.mapper;

import com.lanou.bean.AdminInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminInfoMapper {
    int deleteByPrimaryKey(Integer adminId);

    int insert(AdminInfo record);

    int insertSelective(AdminInfo record);

    AdminInfo selectByPrimaryKey(Integer adminId);

    int updateByPrimaryKeySelective(AdminInfo record);

    int updateByPrimaryKey(AdminInfo record);

    List<AdminInfo> findAdminInfoListByRoleId(@Param("roleId")Integer roleId);

    List<AdminInfo> findAll();

    AdminInfo findByAdminCode(@Param("adminCode") String adminCode);

    void saveAdminRole(@Param("adminId") Integer adminId,
                       @Param("roleId") Integer roleId);

    AdminInfo findAdminByIdWithCascade(@Param("adminId") Integer adminId);

    void deleteAdminRolesByAdminId(@Param("adminId") Integer adminId);

    // 模糊查询
    List<AdminInfo> selectByInfo(@Param("moduleId") Integer moduleId,
                                 @Param("roleName") String roleName);
}