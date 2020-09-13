package com.biao.mebatis.v1;

public class TestMebatis {

    public static void main(String[] args) {
        SqlSession sqlSession = new SqlSession(new Executor(), new Configuration());
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.selectUserById(1);
    }
}
