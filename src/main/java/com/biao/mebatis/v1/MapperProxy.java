package com.biao.mebatis.v1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy implements InvocationHandler {

    private SqlSession sqlSession;

    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String clazzName = method.getDeclaringClass().getName();
        String name = method.getName();
        return sqlSession.selectOne(clazzName + "." + name, args[0]);
    }
}
