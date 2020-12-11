package com.bjpowernode.settings.web.controller;

import com.bjpowernode.settings.domain.User;

import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.utils.MD5Util;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户控制器");

        String path = request.getServletPath();

        // 判断是否为登录操作,后面的else判断是否为注册操作
        if("/settings/user/login.do".equals(path)){

            System.out.println("进入到验证登录操作");
            String loginAct = request.getParameter("loginAct");
            String loginPwd = request.getParameter("loginPwd");
            // 将密码的明文形式转换为MD5的密文形式
            loginPwd = MD5Util.getMD5(loginPwd);
            // 接收ip地址
            String ip = request.getRemoteAddr();
            System.out.println("----ip:"+ip);

            // 未来的业务层开发,统一使用代理类形态的接口对象
            UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

            try{
                User user = us.login(loginAct,loginPwd,ip);

                request.getSession().setAttribute("user",user);

                // 如果程序执行到此处,说明业务层没有为controller抛出任何的异常
                // 表示登录成功
                /**
                 * {"seccess":true}
                 */
                PrintJson.printJsonFlag(response,true);

            }catch(Exception e){
                e.printStackTrace();

                // 一旦程序执行了catch块的信息,说明业务层为我们验证登录失败,为controller抛出了异常
                // 表示登录失败
                /**
                 * {"success":false,"msg":"哪错了?"}
                 */
                String msg = e.getMessage();
                /**
                 * 我们现在作为controller,需要为ajax请求提供多项信息
                 * 可以有两种手段来处理:
                 *      (1) 将多项信息打包成为map,将map解析为json串
                 *      (2) 创建一个vo
                 *          private boolean success;
                 *          private String msg;
                 *
                 *      如果对于展现的信息将来还会大量的使用,我们创建一个vo类使用方便
                 *      如果对于展现的信息只有在这个需求中能够使用,那么我们使用map就可以了
                 */
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("success",false);
                map.put("msg",msg);
                PrintJson.printJsonObj(response,map);
            }

        }else if("/settings/user/xxx.do".equals(path)){

        }

    }
}
