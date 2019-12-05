package com.itheima.dao;

import com.itheima.pojo.User;

public interface UserDao {
    User findByname(String username);
}
