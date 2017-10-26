package com.lanou.mapper;

import com.lanou.bean.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {
    int deleteByPrimaryKey(Integer accountId);

    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(Integer accountId);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    List<Account> findAll();

    List<Account> selectByInfo(@Param("idcardNo")String idcardNo,
                               @Param("realName")String realName,
                               @Param("loginName")String loginName,
                               @Param("status")String status);

    Account findAccountByIdCard(@Param("idcardNo") String idcardNo);

    Account findAccountByLoginName(@Param("loginName") String loginName);

    Account findAccountByIdWithCascade(@Param("accountId") Integer id);
}