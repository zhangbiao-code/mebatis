package com.biao.mebatis.v1;

public class User {

    public int id;

    public String nickName;

    public String password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
