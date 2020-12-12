package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.vo.PaginationVo;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.service.ActivityService;
import com.bjpowernode.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到市场活动控制器");

        String path = req.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){

            getUserList(req,resp);

        }else if("/workbench/activity/save.do".equals(path)){

            save(req,resp);

        }else if("/workbench/activity/pageList.do".equals(path)){

            pageList(req,resp);

        }
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询市场活动信息列表的操作(结合条件查询+分页查询)");

        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String pageNoStr = req.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        // 每页展现的记录树
        String pageSizeStr = req.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        // 计算出略过的记录树
        int skipCount = (pageNo-1) * pageSize;

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        // 因为一下两条信息不在domain类中,所以选择使用map进行传值
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /**
         * 前端要: 市场活动信息列表
         *          查询的总条数
         *
         *          业务层拿到了以上两项信息之后,如何做返回
         *          map
         *          map.put("dataList",dataList);
         *          map.put("total",total);
         *          PrintJson map --> json
         *          {"total":100,"dataList":[{市场活动1},{2},{3}...]
         *
         *          vo
         *          PaginationVo<T>
         *              private int total;
         *              private List<T> dataList;
         *
         *          PaginationVo<Activity> vo = new PaginationVo<>();
         *          vo.setTotal(total);
         *          vo.setDataList(dataList);
         *          PrintJson vo --> json
         *          {"total":100,"dataList":[{市场活动1},{2},{3}...]}
         *
         *          将来分页查询: 每个模块都有,所以我们选择使用一个通用的vo,操作起来比较方便
         */
        PaginationVo<Activity> vo = as.pageList(map);

        // vo --> {"total":total,"dataList":[{市场活动1},{2},{3}...]}
        PrintJson.printJsonObj(resp,vo);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场活动的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        // 创建时间: 当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人: 当前登录用户
        String createBy = ((User) req.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());


        boolean flag = as.save(activity);

        PrintJson.printJsonFlag(resp,flag);


    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(resp,uList);
    }

}
