package com.bjpowernode.workbench.dao;

public interface ActivityRemarkDao {
    Integer getCountByAids(String[] ids);

    Integer deleteByAids(String[] ids);
}
