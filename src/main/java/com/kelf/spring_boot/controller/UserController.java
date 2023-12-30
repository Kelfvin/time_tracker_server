package com.kelf.spring_boot.controller;

import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;



    /// 检查登录
    @ApiOperation("检查登录")
    @GetMapping("/checkLogin")
    public Result checkLogin(String token) {
        if (token == null) {
            return Result.error().message("未登录");
        }
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Result.error().message("未登录");
        }
        return Result.ok().data("username", username);
    }


    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody User user) {

        User userSearched = userMapper.selectByUsername(user.getUsername());

        System.out.println(userSearched);

        // 判断用户名和密码
        if (userSearched == null || !userSearched.getPassword().equals(user.getPassword())) {
            return Result.error().message("用户名或密码错误");
        }

        // 生成token
        String token;
        token = JwtUtils.generateToken(user.getUsername());
        return Result.ok().data("token", token)
                .data("username", user.getUsername());
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result getInfo(String token) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        return Result.ok().data("roles", new String[]{"admin"})
                .data("name", user.getUsername())
                .data("avatar", user.getAvatar());
    }

    // 用户注册
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 先判断用户名是否已经存在
        User userSearched = userMapper.selectByUsername(user.getUsername());
        if (userSearched != null) {
            return Result.error().message("用户名已存在");
        }
        // 插入用户
        userMapper.insert(user);
        return Result.ok();
    }


    @ApiOperation("获取所有用户信息")
    @GetMapping("/findAll")
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }



}

