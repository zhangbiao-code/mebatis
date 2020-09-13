package com.biao.mebatis.v2.mapper;

import com.biao.mebatis.v2.annotation.Select;

public interface UserMapper {

    @Select("select id,nick_name,password from user where id = ?")
    User selectUserById(int id);
}
