package com.lanou.service;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.Account;

import java.util.List;


/**
 * Created by dllo on 17/10/23.
 */
public interface AccountService {

    /*分页:2nd*/
    PageInfo<Account> AccountPageinfo(Integer pageNo, Integer pageSize);

    void addNew(Account account);

    void updateAccount(Account account);

    Account findAccountById(Integer accountId);

    PageInfo<Account> selectByInfo(String idcardNo, String realName, String loginName, String status,Integer pageNo,Integer pageSize);

    Account findAccountByIdCard(String idcardNo);

    Account findAccountByLoginName(String loginName);

    Account findAccountByIdWithCascade(Integer id);
}
