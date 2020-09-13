package com.biao.mebatis.v2.session;

import com.biao.mebatis.v2.executor.Executor;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        // 根据全局配置决定是否使用缓存装饰
        this.executor = configuration.newExecutor();
    }

    public <T> T getMapper(Class<T> clazz) {
        return configuration.getMapper(clazz, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    public <T> T selectOne(String statement, Object[] parameter, Class pojo) {
        return executor.query(statement, parameter, pojo);
    }
}
