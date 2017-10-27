package com.lanou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lanou.bean.RoleInfo;
import com.lanou.mapper.RoleInfoMapper;
import com.lanou.service.RoleInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dllo on 17/10/26.
 */
@Service
public class RoleInfoServiceImpl implements RoleInfoService {

    @Resource
    private RoleInfoMapper roleInfoMapper;

    // 获取分页信息+已经分页的显示全部功能
    @Override
    public PageInfo<RoleInfo> roleInfoPageInfo(Integer pageNo, Integer pageSize) {
        // 判断参数的合法性
        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?5:pageSize;

        PageHelper.startPage(pageNo,pageSize);

        // 获取数据库中的roleInfo信息,记得要用级联
        List<RoleInfo> roleInfoList = roleInfoMapper.findAll();

        PageInfo<RoleInfo> roleInfoPageInfo = new PageInfo<RoleInfo>(roleInfoList);

        return roleInfoPageInfo;
    }

    // 通过id查找,用了级联查找关联的moduleInfoList
    @Override
    public RoleInfo findRoleInfoById(Integer id) {
        // 这里使用自己写的方法,因为要用多对多级联关系
        RoleInfo roleInfoById = roleInfoMapper.findRoleInfoById(id);
        return roleInfoById;
    }

    @Override
    public void updateRole(RoleInfo roleInfo) {
        roleInfoMapper.updateByPrimaryKey(roleInfo);
    }

    @Override
    public void deleteRoleModules(Integer id) {
        roleInfoMapper.deleteRoleModules(id);
    }

    @Override
    public void saveRoleModules(Integer roleId, Integer moduleId) {
        roleInfoMapper.insertRoleModule(roleId,moduleId);
    }

    @Override
    public void addRole(RoleInfo roleInfo) {
        roleInfoMapper.insertSelective(roleInfo);
    }

    @Override
    public RoleInfo findRoleByRoleName(String roleName) {
        return roleInfoMapper.findRoleByRoleName(roleName);
    }
}
