package com.kelf.spring_boot.controller;


import com.kelf.spring_boot.entity.Category;
import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.CategoryMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryMapper categoryMapper;

    // 增加分类
    @ApiOperation("增加分类")
    @PostMapping("/add")
    public Result add(@RequestBody Category category) {
        // 先验证类别的用户id是否和当前用户id一致
        //
        //
        User user = category.getUser();//有问题不准确

        categoryMapper.addCategory(user.getId(),category);


        return Result.ok();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/id")
    public Result deleteCategory(int id){
        categoryMapper.deleteCategory(id);
        return Result.ok();
    }

    @ApiOperation("查询所有分类")
    @GetMapping("/findAll")
    public Result findAllCategory(int userId){
        //怎么获得这个userid
        categoryMapper.getAllCategoryByUserId(userId);
        return Result.ok();
    }

    @ApiOperation("根据id查询分类")
    @GetMapping("/id")
    public Result selectByID(){
        return Result.ok();
    }



}
