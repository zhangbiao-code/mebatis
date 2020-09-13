package com.biao.mebatis.v2;

import com.biao.mebatis.v2.mapper.User;
import com.biao.mebatis.v2.mapper.UserMapper;
import com.biao.mebatis.v2.session.SqlSession;
import com.biao.mebatis.v2.session.SqlSessionFactory;

public class Test {

    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactory().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectUserById(1);
        System.out.println(user);

        User user2 = userMapper.selectUserById(1);
        System.out.println(user2);
    }
}
