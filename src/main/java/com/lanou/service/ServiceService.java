package com.lanou.service;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.Service;

/**
 * Created by dllo on 17/10/25.
 */
public interface ServiceService {
    PageInfo<Service> selectByInfo(String osUsername, String unixHost, String idcardNo, String status, Integer pageNo, Integer pageSize);

    void addNew(Service service);
}
