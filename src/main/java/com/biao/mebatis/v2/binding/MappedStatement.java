package com.biao.mebatis.v2.binding;

public class MappedStatement {

    private String id;

    private SqlCommandType type;

    public MappedStatement(String sql) {
        this.id = sql;
        this.type = SqlCommandType.getType(sql);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getType() {
        return type;
    }

    public void setType(SqlCommandType type) {
        this.type = type;
    }
}
