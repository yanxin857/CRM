package com.bjpowernode;

import com.bjpowernode.exception.LoginException;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.MD5Util;

public class Test1 {
    public static void main(String[] args) {

        // 验证失效时间
        // 失效时间
        /*String expireTime = "2021-12-12 00:00:00";
        // 当前系统时间
        String str = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(str);
        System.out.println(count);*/

        /*String lockState = "0";
        if("0".equals(lockState)){
            System.out.println("账号已锁定");
        }*/

        // 浏览器端的ip地址
        /*String ip = "192.168.1.1";
        // 允许访问的ip地址群
        String allowIps = "192.168.1.1,192.168.1.2";

        if(allowIps.contains(ip)){
            System.out.println("有效的ip地址,允许访问系统");
        }else{
            System.out.println("ip地址受限,请联系管理员");
        }*/

        /*String pwd = "123";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);*/
    }
}
