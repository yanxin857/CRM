package com.bjpowernode.settings;

import com.bjpowernode.utils.ServiceFactory;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.service.ActivityService;
import com.bjpowernode.workbench.service.impl.ActivityServiceImpl;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * JUnit:
 *      单元测试
 *      是未来实际项目开发中,用来代替主方法main的
 *
 */
public class ActivityTest {

    @Test
    public void testSave(){

        Activity activity = new Activity();
        activity.setName("抖音广告");
        activity.setId(UUIDUtil.getUUID());

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        as.save(activity);

    }
    @Test
    public void testDelete(){

    }
}
