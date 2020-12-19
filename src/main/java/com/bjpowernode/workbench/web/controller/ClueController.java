package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.DicService;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.DicServiceImpl;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.vo.PaginationVo;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.ActivityRemark;
import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.service.ActivityService;
import com.bjpowernode.workbench.service.ClueService;
import com.bjpowernode.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.workbench.service.impl.ClueServiceImpl;

import javax.security.auth.login.CredentialException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = req.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)) {

            getUserList(req,resp);

        }else if("/workbench/clue/save.do".equals(path)){

            save(req,resp);

        }else if("/workbench/clue/detail.do".equals(path)){
            
            detail(req,resp);
            
        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){

            getActivityListByClueId(req,resp);

        }else if("/workbench/clue/unbund.do".equals(path)){
            
            unbund(req,resp);
            
        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){

            getActivityListByNameAndNotByClueId(req,resp);

        }else if("/workbench/clue/bund.do".equals(path)){

            bund(req,resp);

        }else if("/workbench/clue/getActivityListByName.do".equals(path)){

            getActivityListByName(req,resp);

        }else if("/workbench/clue/convert.do".equals(path)){

            convert(req,resp);

        }
    }

    private void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("执行线索转换操作");

        // 接收是否需要创建交易的标记
        String flag = req.getParameter("flag");
        String clueId = req.getParameter("clueId");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        Tran t = null;

        if("a".equals(flag)){ // 注意啊兄弟,字符串之间比较是否相等,是需要使用equals()函数的!!

            // 接收交易表单中的参数
            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t = new Tran();

            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /**
         * 为业务层传递的参数
         *  1. 必须传递的参数clueId,有了这个clueId之后,我们才知道要转换哪条记录
         *  2. 必须传递的参数t,因为在线索转换的过程中,有可能会临时创建一笔交易(业务层接收的t也有可能是个null)
         */
        boolean flag1 = cs.convert(clueId,t,createBy);

        if(flag1){
            // 重定向
            resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

    private void getActivityListByName(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("查询市场活动列表(根据名称模糊查询)");;

        String aname = req.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(resp,aList);
    }

    private void bund(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("");

        String cid = req.getParameter("cid");
        String[] aids = req.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid,aids);

        PrintJson.printJsonFlag(resp,flag);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("查询市场活动列表(根据名称模糊查+排除掉已经关联指定线索的列表)");

        String aname = req.getParameter("aname");
        String clueId = req.getParameter("clueId");

        Map<String,String> map = new HashMap<String, String>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);

        PrintJson.printJsonObj(resp,aList);
    }

    private void unbund(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行解除关联操作");

        String id = req.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(resp,flag);

    }

    private void getActivityListByClueId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据线索id查询关联的市场活动列表");

        String clueId = req.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(resp,aList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("跳转到线索的详细信息页");

        String id = req.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = cs.detail(id);

        req.setAttribute("c",clue);

        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行线索的添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        Clue clue = new Clue();
        clue.setId(id);
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setPhone(phone);
        clue.setOwner(owner);
        clue.setNextContactTime(nextContactTime);
        clue.setMphone(mphone);
        clue.setJob(job);
        clue.setFullname(fullname);
        clue.setEmail(email);
        clue.setDescription(description);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setContactSummary(contactSummary);
        clue.setCompany(company);
        clue.setAppellation(appellation);
        clue.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

         boolean flag = cs.save(clue);

         PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(resp,uList);
    }
}