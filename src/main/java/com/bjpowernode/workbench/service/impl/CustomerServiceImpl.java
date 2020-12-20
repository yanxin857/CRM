package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.workbench.dao.CustomerDao;
import com.bjpowernode.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);
        return sList;
    }
}
