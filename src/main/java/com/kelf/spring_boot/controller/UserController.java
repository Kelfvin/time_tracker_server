package com.kelf.spring_boot.controller;

import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.FileUtils;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserMapper userMapper;


    // 根据是Windows还是Linux系统，设置不同的文件上传路径
    @Value("${upload.windows.path}")
    String windowsUploadPath;

    @Value("${upload.linux.path}")
    String linuxUploadPath;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

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
    public Result getAllUser() {

        return Result.ok().data("users",userMapper.getAllUser());
    }



    @ApiOperation("更新用户信息")
    @PutMapping("")
    public Result update(@RequestBody User user){
        //先简单定义一个
        try {
            userMapper.updateUser(user);
        }catch(Exception e){
            return Result.error().message(e.getMessage());
        }
        return Result.ok();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("")
    public Result deleteUser(@RequestBody User user){
        try{
            userMapper.deleteUser(user.getId());
        }catch(Exception e){
            return Result.error().message(e.getMessage());
        }
        return Result.ok();
    }

    // 修改头像
    @ApiOperation("修改头像")

    @PutMapping("/updateAvatar")
public Result updateAvatar(String token, MultipartFile avatar) {

        // 文件检验
        if (avatar == null) {
            return Result.error().message("文件不能为空");
        }
        if (avatar.getSize() > 1024 * 1024 * 5) {
            return Result.error().message("文件不能大于5M");
        }

        // 文件的类型检验，必须是图片
        String contentType = avatar.getContentType();

        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            return Result.error().message("文件格式不正确");
        }


        // 获取用户名
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);

        // filename 是用户id+原始文件名后缀
        String fileName = user.getId() + avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf("."));


        // 保存文件
        // 看是Windows还是Linux系统
        String os = System.getProperty("os.name");

        if (os.toLowerCase().startsWith("win")) {
            // Windows系统
            try {
                FileUtils.saveFile(avatar, windowsUploadPath,fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error().message("文件保存失败");
            }
        } else {
            // Linux系统
            try {
                FileUtils.saveFile(avatar, linuxUploadPath,fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error().message("文件保存失败");
            }
        }

        // 更新用户头像
        user.setAvatar("/upload/" + fileName);


        userMapper.updateUser(user);

        return Result.ok().data("avatar", user.getAvatar());

    }




}

