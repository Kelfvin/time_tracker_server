package com.kelf.spring_boot.controller;


import com.kelf.spring_boot.entity.Category;
import com.kelf.spring_boot.mapper.CategoryMapper;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("增加分类")
    @PostMapping("/add")
    public Result add(@RequestBody Category category,String token) {
        // 先验证类别的用户id是否和当前用户id一致
        String username = JwtUtils.getClaimsByToken(token).getSubject();

//        return Result.ok().data("username",username).data("category",userMapper.selectByUsername(username).getId());
        if(username.equals(category.getUser().getUsername())){
            categoryMapper.addCategory(userMapper.selectByUsername(username).getId(),category);
        }else{
            return Result.error().message("非当前登录用户");
        }
        return Result.ok().message("增加分类成功");

    }

    @ApiOperation("删除分类")
    @DeleteMapping("/delete")
    public Result deleteCategory(int id,String token){
        // 先验证类别的用户id是否和当前用户id一致
        String username = JwtUtils.getClaimsByToken(token).getSubject();
//        if(categoryMapper.getCategoryById(id)==null ||username.equals(categoryMapper.getCategoryById(id).getUser().getUsername())){
//            categoryMapper.deleteCategory(id);
//        }else{
//            return Result.error().message("非当前登录用户");
//        }
//        return Result.ok().message("删除分类成功");
        return Result.ok().data("data",categoryMapper.getCategoryById(id));

    }


    @ApiOperation("根据id查询分类")
    @GetMapping("/user")
    public Result selectByID(String token){
        String username =  JwtUtils.getClaimsByToken(token).getSubject();
        UserMapper userMapper = null;
        int user_id = userMapper.selectByUsername(username).getId();
        return Result.ok().data("category",categoryMapper.getAllCategoryByUserId(user_id));

    }



}
