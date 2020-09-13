package com.biao.mebatis.v2.executor;

public interface Executor {

    <T> T query(String statement, Object[] parameter, Class pojo);
}
