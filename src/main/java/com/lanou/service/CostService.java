package com.lanou.service;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.Cost;

import java.util.List;

/**
 * Created by dllo on 17/10/20.
 */
public interface CostService {

    List<Cost> findAll();

    void addNew(Cost cost);

    Cost findCostById(Integer id);

    void deleteCostById(Integer id);

    void updateCost(Cost cost);

    /*分页:2nd*/
    PageInfo<Cost> costPageinfo(Integer pageNo,Integer pageSize);

    List<Cost> sortCost(Integer type, Integer order);

    PageInfo<Cost> sortCostWithPage(Integer type, Integer order,Integer pageNo, Integer pageSize);
}
