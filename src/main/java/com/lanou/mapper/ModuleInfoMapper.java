package com.lanou.mapper;

import com.lanou.bean.ModuleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleInfoMapper {
    int deleteByPrimaryKey(Integer moduleId);

    int insert(ModuleInfo record);

    int insertSelective(ModuleInfo record);

    ModuleInfo selectByPrimaryKey(Integer moduleId);

    int updateByPrimaryKeySelective(ModuleInfo record);

    int updateByPrimaryKey(ModuleInfo record);

    List<ModuleInfo> findModuleInfoListByRoleId(@Param("roleId") Integer roleId);
}