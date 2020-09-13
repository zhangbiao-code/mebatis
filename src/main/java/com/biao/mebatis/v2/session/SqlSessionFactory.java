package com.biao.mebatis.v2.session;

public class SqlSessionFactory {

    private Configuration configuration;

    public SqlSessionFactory build() {
        this.configuration = new Configuration();
        return this;
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
