package com.biao.mebatis.v1;

public class SqlSession {

    private Executor executor;

    private Configuration configuration;

    public SqlSession(Executor executor, Configuration configuration) {
        this.executor = executor;
        this.configuration = configuration;
    }

    public <T> T selectOne(String statementId, Object param) {
        String sql = Configuration.sqlMappings.getString(statementId);
        if (sql != null && !"".equals(sql)) {
            return executor.query(sql, param);
        }
        return null;
    }

    public <T> T getMapper(Class clazz) {
        return configuration.getMapper(clazz, this);
    }
}
