package com.lanou.mapper;

import com.lanou.bean.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceMapper {
    int deleteByPrimaryKey(Integer serviceId);

    int insert(Service record);

    int insertSelective(Service record);

    Service selectByPrimaryKey(Integer serviceId);

    int updateByPrimaryKeySelective(Service record);

    int updateByPrimaryKey(Service record);

    List<Service> findServiceListByAccountId(@Param("accountId")Integer accountId);

    List<Service> findServiceListByCostId(@Param("costId")Integer costId);

    List<Service> selectByInfo(@Param("osUsername") String osUsername,
                               @Param("unixHost") String unixHost,
                               @Param("idcardNo") String idcardNo,
                               @Param("status") String status);

    Service findServiceDetailsById(@Param("serviceId") Integer id);

    Service findByOsUsername(@Param("osUsername") String osUsername);
}