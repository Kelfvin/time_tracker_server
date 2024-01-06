package com.kelf.spring_boot.service.impl;

import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kelf.spring_boot.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;


    @Override
    public User getUserById(int id) {
        // 查询用户
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        // 查询用户
        return userMapper.selectByUsername(username);
    }

    @Override
    public void addUser(User user) {
        // 增加用户
        userMapper.insert(user);
    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public void updateUser(User user) {

    }
}
