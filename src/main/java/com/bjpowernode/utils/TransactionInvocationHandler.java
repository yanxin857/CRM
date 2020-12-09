package com.bjpowernode.utils;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionInvocationHandler implements InvocationHandler {

    // target: 目标对象
    private Object target;

    public TransactionInvocationHandler(Object target){
        this.target = target;
    }

    // 代理类的业务方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSession sqlSession = null;
        Object obj = null;
        try{
            sqlSession = SqlSessionUtil.getSqlSession();

            // 处理业务逻辑
            // method.invoke 是目标对象的方法
            obj = method.invoke(target,args);

            // 处理业务逻辑完毕后,提交事务
            sqlSession.commit();
        }catch(Exception e){
            sqlSession.rollback();
            e.printStackTrace();
        }finally{
            SqlSessionUtil.close(sqlSession);
        }
        return obj;
    }

    // 取得代理类对象
    public Object getProxy(){

        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this);
    }
}
