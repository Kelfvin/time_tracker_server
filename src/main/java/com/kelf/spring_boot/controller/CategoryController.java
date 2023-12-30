package com.kelf.spring_boot.controller;


import com.kelf.spring_boot.entity.Category;
import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.CategoryMapper;
import com.kelf.spring_boot.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryMapper categoryMapper;

    // 增加分类
    @PostMapping("/add")
    Result add(@RequestBody Category category) {

        // 先验证类别的用户id是否和当前用户id一致


        return Result.ok();
    }

}
