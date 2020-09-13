package com.biao.mebatis.v2.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 参数处理器
 */
public class ParameterHandler {

    private PreparedStatement ps;

    public ParameterHandler(PreparedStatement ps) {
        this.ps = ps;
    }

    /**
     * 遍历sql中的占位符(?) 并替换
     * 这里并未包含所有类型,举例写了常用类型
     *
     * @param args
     */
    public void setParameters(Object[] args) {

        int index = 1;
        try {
            for (Object parameter : args) {
                if (parameter instanceof Integer) {
                    ps.setInt(index, (Integer) parameter);
                } else if (parameter instanceof Boolean) {
                    ps.setBoolean(index, (Boolean) parameter);
                } else if (parameter instanceof Long) {
                    ps.setLong(index, (Long) parameter);
                } else if (parameter instanceof String) {
                    ps.setString(index, (String) parameter);
                } else if (parameter instanceof Double) {
                    ps.setDouble(index, (Double) parameter);
                } else {
                    ps.setString(index, String.valueOf(parameter));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
