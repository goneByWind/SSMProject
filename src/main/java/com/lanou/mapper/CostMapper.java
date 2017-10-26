package com.lanou.mapper;

import com.lanou.bean.Cost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CostMapper {
    int deleteByPrimaryKey(Integer costId);

    int insert(Cost record);

    int insertSelective(Cost record);

    Cost selectByPrimaryKey(Integer costId);

    int updateByPrimaryKeySelective(Cost record);

    int updateByPrimaryKey(Cost record);

    List<Cost> findAll();

    List<Cost> sortWithTypeAndOrder(@Param("type") Integer type, @Param("order") Integer order);

    List<Cost> sortWithBasecostDesc();

    List<Cost> sortWithBasecostAsc();

    List<Cost> sortWithBasedurationDesc();

    List<Cost> sortWithBasedurationAsc();
}