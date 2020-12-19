package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);
}
