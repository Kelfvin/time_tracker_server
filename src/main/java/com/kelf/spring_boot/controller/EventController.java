package com.kelf.spring_boot.controller;

import com.kelf.spring_boot.entity.Event;
import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.EventMapper;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private UserMapper userMapper;


    @ApiOperation("增加事件")
    @PostMapping("/add")
    public Result addEvent(){

        return Result.ok();
    }

    @ApiOperation("根据用户token查询所有事件")
    @GetMapping("/selectByToken")
    public Result getAllEvent(@RequestBody Map<String, Object> requestBody){
        String token = (String) requestBody.get("token");
//        return Result.ok().data("test",token);
        String username =  JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        return Result.ok().data("events",eventMapper.getEventsByUserId(user.getId()));

    }

    @ApiOperation("删除事件")
    @DeleteMapping("/id")
    public Result deleteEvent(){
        return Result.ok();
    }

    @ApiOperation("修改事件")
    @PutMapping("")
    public Result updateEvent(){
        return Result.ok();
    }




}
