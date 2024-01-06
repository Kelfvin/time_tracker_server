package com.kelf.spring_boot.service;

import com.kelf.spring_boot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  com.kelf.spring_boot.entity.User;


public interface UserService {
    // 根据用户id获取用户
    public User getUserById(int id);

    // 根据用户名获取用户
    public User getUserByUsername(String username);

    // 增加用户
    public void addUser(User user);

    // 删除用户
    public void deleteUser(int id);

    // 更新用户
    public void updateUser(User user);


}
