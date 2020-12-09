package com.bjpowernode;

import com.bjpowernode.utils.DateTimeUtil;

public class Test1 {
    public static void main(String[] args) {

        // 验证失效时间
        // 失效时间
        String expireTime = "2021-12-12 00:00:00";
        // 当前系统时间
        String str = DateTimeUtil.getSysTime();

        int count = expireTime.compareTo(str);

        System.out.println(count);
    }
}
