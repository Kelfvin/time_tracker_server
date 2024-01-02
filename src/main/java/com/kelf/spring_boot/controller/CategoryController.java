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

    public CategoryController(CategoryMapper categoryMapper, UserMapper userMapper) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }

    // 增加分类
    @ApiOperation("根据传入的category和token增加分类")
    @PostMapping("/add")
    public Result add(@RequestBody Map<String, Object> requestBody) {
/** 传入以下类型的就可以
        {
            "category":{
            "name":"吃饭",
                    "user":{
                "username":"test",
                        "password":"test"
            }
        },
            "token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzA0MTc0MTU0LCJleHAiOjE3MDQ3Nzg5NTR9.3VL9Z9eeOGUMDVcEGyxiXIZjdvx-kIMAU0wddIsc78c"
        }
 **/
        // 从requestBody中获取category对象数据
        Map<String, Object> categoryData = (Map<String, Object>) requestBody.get("category");
        Category category = new Category();
        category.setName((String) categoryData.get("name"));
        Map<String,Object> category_user = (Map<String, Object>) categoryData.get("user");
        if(category_user.get("username") != null){
            User user = userMapper.selectByUsername((String) category_user.get("username"));
            category.setUser(user);
        }
        //获取token
        String token = (String) requestBody.get("token");


        // 先验证类别的用户id是否和当前用户id一致户id是否和当前用户id一致
        String username = JwtUtils.getClaimsByToken(token).getSubject();

        if(username.equals(category.getUser().getUsername())){
            categoryMapper.addCategory(userMapper.selectByUsername(username).getId(),category);
        }else{
            return Result.error().message("非当前登录用户");
        }
        return Result.ok().message("增加分类成功");
    }


    @ApiOperation("根据token删除分类")
    @DeleteMapping("/delete")
    public Result deleteCategory(@RequestBody Map requestBody){
        int id = (int) requestBody.get("id");;
        String token = (String) requestBody.get("token");
        // 先验证类别的用户id是否和当前用户id一致
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        if(categoryMapper.getCategoryById(id) == null ||username.equals(categoryMapper.getCategoryById(id).getUser().getUsername())){
            categoryMapper.deleteCategory(id);
        }else{
            return Result.error().message("非当前登录用户");
        }
        return Result.ok().message("删除分类成功");


    }


    @ApiOperation("根据token查询分类")
    @GetMapping("/user")
    public Result selectById(@RequestBody Map<String, Object> requestBody){
        String token = (String) requestBody.get("token");
//        return Result.ok().data("test",token);

        String username =  JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
//        return Result.ok().data("id",user.getId());
        return Result.ok().data("category",categoryMapper.getAllCategoryByUserId(user.getId()));

    }



}
