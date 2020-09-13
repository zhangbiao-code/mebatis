package com.biao.mebatis.v2.session;

public interface SqlSession {

    <T> T selectOne(String statement, Object[] parameter, Class pojo);

    <T> T getMapper(Class<T> userMapperClass);

    Configuration getConfiguration();
}
