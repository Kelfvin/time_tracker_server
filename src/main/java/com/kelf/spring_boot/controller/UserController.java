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



    /// 检查token
    @ApiOperation("检查token")
    @GetMapping("/checkToken")
    public Result checkToken(String token) {
        if (JwtUtils.checkToken(token)) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }


    @ApiOperation("登录")
    @CrossOrigin(origins = "*", maxAge = 3600)
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


    @ApiOperation("获取所有用户信息")
    @GetMapping("/findAll")
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }



}

