package com.biao.mebatis.v2.binding;

public enum SqlCommandType {

    UNKNOWN("unknown"),
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    SELECT("select"),
    FLUSH("flush");

    private String value;

    SqlCommandType(String value) {
        this.value = value;
    }

    public static SqlCommandType getType(String sql) {
        for (SqlCommandType type : values()) {
            if (sql.contains(type.value)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
