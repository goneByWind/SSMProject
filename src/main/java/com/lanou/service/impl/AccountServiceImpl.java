package com.lanou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lanou.bean.Account;
import com.lanou.mapper.AccountMapper;
import com.lanou.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dllo on 17/10/23.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    // 分页!
    @Override
    public PageInfo<Account> AccountPageinfo(Integer pageNo, Integer pageSize) {
        // 判断参数的合法性
        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?5:pageSize;

        PageHelper.startPage(pageNo,pageSize);
        List<Account> accountList = accountMapper.findAll();

        // 使用PageInfo对结果进行包装
        PageInfo<Account> accountPageInfo = new PageInfo<Account>(accountList);
        return accountPageInfo;
    }

    @Override
    public void addNew(Account account) {
        int i = accountMapper.insertSelective(account);
        if (i>0){
            System.out.println("添加account账号成功");
        }else {
            System.out.println("添加account账号失败");
        }
    }

    @Override
    public void updateAccount(Account account) {
        accountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    public Account findAccountById(Integer accountId) {
        return accountMapper.selectByPrimaryKey(accountId);
    }

    @Override
    public PageInfo<Account> selectByInfo(String idcardNo, String realName, String loginName, String status,Integer pageNo,Integer pageSize) {

        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?5:pageSize;

        /*一定要放在最前面,放在获得的list对象的前面*/
        PageHelper.startPage(pageNo,pageSize);

        List<Account> accountList = accountMapper.selectByInfo(idcardNo, realName, loginName, status);

        PageInfo<Account> accountPageInfo1 = new PageInfo<Account>(accountList);
        return accountPageInfo1;
    }

    @Override
    public Account findAccountByIdCard(String idcardNo) {
        Account accountByIdCard = accountMapper.findAccountByIdCard(idcardNo);
        return accountByIdCard;
    }

    @Override
    public Account findAccountByLoginName(String loginName) {
        return accountMapper.findAccountByLoginName(loginName);
    }

    @Override
    public Account findAccountByIdWithCascade(Integer id) {
        return accountMapper.findAccountByIdWithCascade(id);
    }
}
