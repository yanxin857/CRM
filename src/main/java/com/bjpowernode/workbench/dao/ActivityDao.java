package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    Integer getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    Integer delete(String[] ids);

    Activity getById(String id);

    int update(Activity a);
}
