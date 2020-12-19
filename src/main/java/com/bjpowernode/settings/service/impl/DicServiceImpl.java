package com.bjpowernode.settings.service.impl;

import com.bjpowernode.settings.dao.DicTypeDao;
import com.bjpowernode.settings.dao.DicValueDao;
import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.domain.DicType;
import com.bjpowernode.settings.domain.DicValue;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.DicService;
import com.bjpowernode.utils.SqlSessionUtil;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<String, List<DicValue>>();

        // 将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        // 将字典类型列表遍历
        for(DicType dicType : dtList){

            // 取得每一种类型的字典类型编码
            String code = dicType.getCode();

            // 根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code+"List",dvList);
        }

        return map;
    }

}
