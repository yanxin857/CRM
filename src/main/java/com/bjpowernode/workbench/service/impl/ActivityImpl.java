package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.workbench.dao.ActivityDao;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.service.ActivityService;

public class ActivityImpl implements ActivityService {
    private ActivityDao activityDao = (ActivityDao) SqlSessionUtil.getSqlSession().getMapper(Activity.class);
}
