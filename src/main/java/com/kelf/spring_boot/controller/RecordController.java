package com.kelf.spring_boot.controller;


import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {

    @ApiOperation("增加记录")
    @PostMapping("")
    public Result createRecord(){return Result.ok();}

    @ApiOperation("查询记录")
    @GetMapping("/findAll")
    public List<Record> findAllRecord(){
        return new ArrayList<>();
    }

    @ApiOperation("删除记录")
    @DeleteMapping("id")
    public Result deleteRecord(){
        return Result.ok();
    }

}
