package com.lanou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lanou.mapper.ServiceMapper;
import com.lanou.service.ServiceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dllo on 17/10/25.
 */
@Service
public class ServiceServiceImpl implements ServiceService {

    @Resource
    private ServiceMapper serviceMapper;

    @Override
    public PageInfo<com.lanou.bean.Service> selectByInfo(String osUsername, String unixHost, String idcardNo, String status, Integer pageNo, Integer pageSize) {

        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?5:pageSize;

        PageHelper.startPage(pageNo,pageSize);

        List<com.lanou.bean.Service> serviceList = serviceMapper.selectByInfo(osUsername, unixHost, idcardNo, status);

        PageInfo<com.lanou.bean.Service> servicePageInfo = new PageInfo<com.lanou.bean.Service>(serviceList);

        return servicePageInfo;
    }

    @Override
    public void addNew(com.lanou.bean.Service service) {
        serviceMapper.insertSelective(service);
    }
}
