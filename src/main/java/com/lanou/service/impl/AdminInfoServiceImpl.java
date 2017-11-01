package com.lanou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lanou.bean.AdminInfo;
import com.lanou.mapper.AdminInfoMapper;
import com.lanou.service.AdminInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dllo on 17/10/28.
 */
@Service
public class AdminInfoServiceImpl implements AdminInfoService {
    @Resource
    private AdminInfoMapper adminInfoMapper;

    @Override
    public PageInfo<AdminInfo> adminPageInfo(Integer pageNo, Integer pageSize,Integer moduleId,String roleName) {
        // 判断参数的合法性
        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?5:pageSize;

        PageHelper.startPage(pageNo,pageSize);

        // 获取数据库中的admin列表信息
        List<AdminInfo> adminInfoList = adminInfoMapper.selectByInfo(moduleId,roleName);

        PageInfo<AdminInfo> adminInfoPageInfo = new PageInfo<AdminInfo>(adminInfoList);

        return adminInfoPageInfo;
    }

    @Override
    public void addNewAdmin(AdminInfo adminInfo) {
        adminInfoMapper.insertSelective(adminInfo);
    }

    @Override
    public AdminInfo findByAdminCode(String adminCode) {
        return adminInfoMapper.findByAdminCode(adminCode);
    }

    @Override
    public void saveAdminRole(Integer adminId, Integer roleId) {
        adminInfoMapper.saveAdminRole(adminId,roleId);
    }

    @Override
    public AdminInfo findAdminByIdWithCascade(Integer adminId) {
        return adminInfoMapper.findAdminByIdWithCascade(adminId);
    }

    @Override
    public void updateAdmin(AdminInfo adminInfo) {
        adminInfoMapper.updateByPrimaryKeySelective(adminInfo);
    }

    @Override
    public void deleteAdminRoles(Integer adminId) {
        adminInfoMapper.deleteAdminRolesByAdminId(adminId);
    }

    @Override
    public void deleteAdminInfoById(Integer adminId) {
        adminInfoMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public AdminInfo findAdminByName(String adminName) {
        return adminInfoMapper.findAdminByName(adminName);
    }

    @Override
    public AdminInfo findAdminByAdminCode(String adminCode) {
        return adminInfoMapper.findAdminByAdminCode(adminCode);
    }
}
