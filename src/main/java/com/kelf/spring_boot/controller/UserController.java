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


    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
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
                .data("avatar", user.getAvatarName());
    }


    @ApiOperation("获取所有用户信息")
    @GetMapping("/findAll")
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }

    @ApiOperation("添加用户")
    @PostMapping("/")
    public String insertUser(@RequestBody User user) {
        userMapper.insert(user);
        System.out.println(user.getId());
        return "success";
    }


}

