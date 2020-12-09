package com.bjpowernode.settings.service.imol;

import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.utils.SqlSessionUtil;

public class UserServiceImpl extends UserService {
    private UserDao dao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

}
