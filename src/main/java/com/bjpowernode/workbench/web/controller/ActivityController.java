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
import com.bjpowernode.workbench.domain.ActivityRemark;
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

        }else if("/workbench/activity/delete.do".equals(path)){

            delete(req,resp);

        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){

            getUserListAndActivity(req,resp);

        }else if("/workbench/activity/update.do".equals(path)){

            update(req,resp);

        }else if("/workbench/activity/detail.do".equals(path)){

            detail(req,resp);

        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){

            getRemarkListByAid(req,resp);

        }else if("/workbench/activity/deleteRemarkById.do".equals(path)){

            deleteRemarkById(req,resp);

        }else if("/workbench/activity/saveRemark.do".equals(path)){

            saveRemark(req,resp);

        }else if("/workbench/activity/updateRemark.do".equals(path)){

            updateRemark(req,resp);

        }
    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行修改备注的操作");

        String id = req.getParameter("id");
        String noteContent = req.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.updateRemark(ar);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(resp,map);
    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行添加备注操作");
        String noteContent = req.getParameter("noteContent");
        String activityId = req.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setActivityId(activityId);
        ar.setNoteContent(noteContent);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(ar);

        /**
         * data
         *      {"success":true/false,"ar":{备注}}
         */
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(resp,map);
    }

    private void deleteRemarkById(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("删除备注操作");

        String id = req.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemarkById(id);

        PrintJson.printJsonFlag(resp,flag);
    }

    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据市场活动id,取得备注信息列表");

        String activityId = req.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(resp,arList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页的操作");

        String id = req.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = as.detail(id);

        req.setAttribute("activity",activity);

        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
    }


    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动修改操作");
        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        // 修改时间: 当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        // 修改人: 当前登录用户
        String editBy = ((User) req.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());


        boolean flag = as.update(a);

        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");

        String id = req.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /**
         * 总结:
         *      controller调用service的方法,返回值是什么
         *      你得想一想前端要什么,就要从service层取什么
         *
         *  前端需要的,管业务层去要
         * uList
         * a
         *
         * 以上两项信息,复用率不高,我们选择使用map打包这两项信息即可
         * map
         */
        Map<String,Object> map = as.getUserListAndActivity(id);
        PrintJson.printJsonObj(resp,map);
    }


    private void delete(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场活动的删除操作");
        String[] ids = req.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询市场活动信息列表的操作(结合条件查询+分页查询)");

        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String pageNoStr = req.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        // 每页展现的记录数
        String pageSizeStr = req.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        // 计算出略过的记录数
        int skipCount = (pageNo-1) * pageSize;

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        // 因为以下两条信息不在domain类中,所以选择使用map进行传值(<parameterType>传值不能使用vo类,<resultType>传值可以使用vo类)
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
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

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
