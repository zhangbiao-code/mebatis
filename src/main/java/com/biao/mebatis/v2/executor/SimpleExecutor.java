package com.biao.mebatis.v2.executor;

/**
 * 最基础的一个执行器
 */
public class SimpleExecutor implements Executor {

    @Override
    public <T> T query(String statement, Object[] parameter, Class pojo) {
        StatementHandler statementHandler = new StatementHandler();
        return statementHandler.query(statement, parameter, pojo);
    }
}
