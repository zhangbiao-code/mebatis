package com.biao.mebatis.v2.executor;

import com.biao.mebatis.v2.parameter.ParameterHandler;
import com.biao.mebatis.v2.session.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 用于处理参数和结果集
 */
public class StatementHandler {

    private ResultSetHandler resultSetHandler = new ResultSetHandler();

    public <T> T query(String statement, Object[] args, Class pojo) {
        Connection connection = null;
        PreparedStatement ps = null;
        Object result = null;
        try {
            connection = getConnection();
            ps = connection.prepareStatement(statement);
            ParameterHandler parameterHandler = new ParameterHandler(ps);
            parameterHandler.setParameters(args);

            ps.execute();
            result = resultSetHandler.handle(ps.getResultSet(), pojo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                connection = null;
            }
        }
        return (T) result;
    }

    private Connection getConnection() {

        String driver = Configuration.properties.getString("jdbc.driver");
        String url = Configuration.properties.getString("jdbc.url");
        String userName = Configuration.properties.getString("jdbc.userName");
        String password = Configuration.properties.getString("jdbc.password");

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
