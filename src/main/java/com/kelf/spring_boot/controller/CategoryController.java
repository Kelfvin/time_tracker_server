package com.kelf.spring_boot.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.kelf.spring_boot.entity.Category;
import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.CategoryMapper;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private  CategoryMapper categoryMapper;
    @Autowired
    private  UserMapper userMapper;


    // 增加分类
    @ApiOperation("根据传入的category和token增加分类")
    @PostMapping("/add")
    public Result add(@RequestHeader("Authorization") String token, @RequestBody Category category) {


        // 获取用户id
        String username = JwtUtils.getClaimsByToken(token).getSubject();


        User user = userMapper.selectByUsername(username);
        category.setUserId(user.getId());
        categoryMapper.addCategory(category);

        return Result.ok().message("增加分类成功").data("category",category);

    }



    @ApiOperation("根据token删除分类")
    @DeleteMapping("/deleteById")
    public Result deleteCategory(@RequestHeader("Authorization") String token,int id){
        // 先验证类别的用户id是否和当前用户id一致
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);

        if (user.getId() == categoryMapper.getCategoryById(id).getUserId()) {
            categoryMapper.deleteCategory(id);
        } else {
            return Result.error().message("非当前登录用户");
        }

//        if(categoryMapper.getCategoryById(categoryId) == null || username.equals(categoryMapper.getCategoryById(categoryId).getUser().getUsername())){
//            categoryMapper.deleteCategory(categoryId);
//        }else{
//            return Result.error().message("非当前登录用户");
//        }
        return Result.ok().message("删除分类成功");


    }


    @ApiOperation("根据token查询分类")
    @GetMapping("/getAllCategory")
    public Result selectById(@RequestHeader("Authorization") String token){

        System.out.println(token);
        String username =  JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        return Result.ok().data("categoryList",categoryMapper.getAllCategoryByUserId(user.getId()));
    }

}
