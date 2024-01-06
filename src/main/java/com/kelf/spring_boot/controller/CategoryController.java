package com.kelf.spring_boot.controller;



import com.kelf.spring_boot.entity.Category;
import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.CategoryMapper;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

        if (user.getId() == categoryMapper.selectById(id).getUserId()) {
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


    // 根据开始时间和截至时间获取统计数据
    @ApiOperation("根据开始时间和截至时间获取统计数据")
    @GetMapping("/getStatistics")
    public Result getStatistics(@RequestHeader("Authorization") String token, String startTime, String endTime) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        // 获取用户的分类
        List<Category> categories = categoryMapper.getAllCategoryByUserId(user.getId());
        // 获取用户的记录

        //TODO: 2024/1/6 0020  这里需要修改, 需要在Category实体中增加一个字段，表示统计的Duration（时间片段长度）
        // 然后根据数据库查询出来的记录，把每条记录的时间片段长度加到对应的分类中
        // 准确的来说 category下有events，每个event下也有Duration
        // Category
        //     List<Event>
        // Event
        //     Duration
        // 思路应该是用分组查询什么的，具体看你怎么写

        return  Result.ok();
    }


}
