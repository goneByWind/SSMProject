package com.lanou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lanou.bean.Cost;
import com.lanou.mapper.CostMapper;
import com.lanou.service.CostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dllo on 17/10/20.
 */
@Service
public class CostServiceImpl implements CostService {

    @Resource
    private CostMapper costMapper;

    @Override
    public List<Cost> findAll() {
        return costMapper.findAll();
    }

    @Override
    public void addNew(Cost cost) {
        int i = costMapper.insertSelective(cost);
        if (i>0){
            System.out.println("添加资费项目成功");
        }else {
            System.out.println("添加资费项目失败");
        }
    }

    @Override
    public Cost findCostById(Integer id) {
        Cost cost = costMapper.selectByPrimaryKey(id);
        return cost;
    }

    @Override
    public void deleteCostById(Integer id) {
        int i = costMapper.deleteByPrimaryKey(id);
        if (i>0){
            System.out.println("删除资费项目成功");
        }else {
            System.out.println("删除资费项目失败");
        }
    }

    @Override
    public void updateCost(Cost cost) {
        int i = costMapper.updateByPrimaryKeySelective(cost);
        if (i>0){
            System.out.println("修改资费项目成功");
        }else {
            System.out.println("修改资费项目失败");
        }
    }

    /*分页3th*/
    @Override
    public PageInfo<Cost> costPageinfo(Integer pageNo, Integer pageSize) {
        // 判断参数的合法性
        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?6:pageSize;

        PageHelper.startPage(pageNo,pageSize);

        // 获取数据库中全部的资费项目
        List<Cost> costList = costMapper.findAll();

        // 使用PageInfo对结果进行包装
        PageInfo<Cost> costPageInfo = new PageInfo<Cost>(costList);
        return costPageInfo;
    }

    //<!--按照标志位排序  Type:基费(0)or时长(1) Order:降序(0)or升序(1)-->
    @Override
    public List<Cost> sortCost(Integer type, Integer order) {

        if (type==0&&order==0){
            return costMapper.sortWithBasecostDesc();
        }
        if (type==0&&order==1){
            return costMapper.sortWithBasecostAsc();
        }
        if (type==1&&order==0){
            return costMapper.sortWithBasedurationDesc();
        }
        if (type==1&&order==1){
            return costMapper.sortWithBasedurationAsc();
        }
        return null;
    }

    @Override
    public PageInfo<Cost> sortCostWithPage(Integer type, Integer order,Integer pageNo, Integer pageSize) {

        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?6:pageSize;

        PageHelper.startPage(pageNo,pageSize);

        if (type==0&&order==0){
            List<Cost> costList = costMapper.sortWithBasecostDesc();
            return new PageInfo<Cost>(costList);
        }
        if (type==0&&order==1){
            List<Cost> costList = costMapper.sortWithBasecostAsc();
            return new PageInfo<Cost>(costList);
        }
        if (type==1&&order==0){
            List<Cost> costList = costMapper.sortWithBasedurationDesc();
            return new PageInfo<Cost>(costList);
        }
        if (type==1&&order==1){
            List<Cost> costList = costMapper.sortWithBasedurationAsc();
            return new PageInfo<Cost>(costList);
        }
        return null;
    }
}
