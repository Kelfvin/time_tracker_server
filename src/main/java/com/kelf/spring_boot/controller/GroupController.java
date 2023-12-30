package com.kelf.spring_boot.controller;


import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {
    @ApiOperation("新建分组")
    @PostMapping("")
    public Result createGroup(){
        return Result.ok();
    }

    @ApiOperation("查询分组")
    @GetMapping("/id") //根据xxxid查询分组
    public Result findGroup(){
        return Result.ok();
    }

    @ApiOperation("删除分组")
    @DeleteMapping("id") //根据xxxid删除分组
    public Result deleteGroup(){
        return Result.ok();
    }

}
