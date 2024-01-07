package com.kelf.spring_boot.controller;

import com.kelf.spring_boot.entity.Category;
import com.kelf.spring_boot.entity.Event;
import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.CategoryMapper;
import com.kelf.spring_boot.mapper.EventMapper;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CategoryMapper categoryMapper;


    @ApiOperation("增加事件")
    @PostMapping("/add")
    public Result addEvent(@RequestHeader("Authorization") String token, @RequestBody Event event){
        // 获取用户id
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        event.setUserId(user.getId());

        //  查询category
        Category category = categoryMapper.selectById(event.getCategoryId());
        if(category == null){
            return Result.error().message("类别不存在");
        }
        // 如果用户id和类别的id不一致，则返回错误
        if(user.getId() != category.getUserId()){
            return Result.error().message("非当前登录用户");
        }

        eventMapper.addEvent(event);
        return Result.ok().message("增加事件成功").data("event",event);
    }

    @ApiOperation("根据用户token查询所有事件")
    @GetMapping("/selectByToken")
    public Result getAllEvent(@RequestHeader("Authorization") String token){
        String username =  JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        return Result.ok().data("events",eventMapper.getEventsByUserId(user.getId()));
    }

    @ApiOperation("删除事件")
    @DeleteMapping("/del")
    public Result deleteEvent(@RequestHeader("Authorization") String token,int id){
        // 获取用户
        User user = userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject());
        // 获取事件
        Event event = eventMapper.getEventById(id);

        // 如果用户id和事件的用户id一致，则删除事件
        if(user.getId() == event.getUserId()){
            eventMapper.deleteEvent(id);
            return Result.ok().message("删除事件成功");
        }

        return Result.error().message("非登录用户");
    }



    @ApiOperation("修改事件")
    @PutMapping("/update")
    public Result updateEvent(@RequestBody Map<String, Object> requestBody){
        /**
         {
         "id":12,
         "name":"kelf kiss kiss",
         "token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzA0MTc0MTU0LCJleHAiOjE3MDQ3Nzg5NTR9.3VL9Z9eeOGUMDVcEGyxiXIZjdvx-kIMAU0wddIsc78c"
         }
         */
        int eventId = (Integer) requestBody.get("id");
        String eventName = (String) requestBody.get("name");
        String token = (String) requestBody.get("token");
        if(eventMapper.getUserIdByEventId(eventId) == userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId()){
            Event event = eventMapper.getEventById(eventId);
            event.setName(eventName);
            eventMapper.updateEvent(event);
            return Result.ok().message("修改事件成功");
        }

        return Result.error().message("非登录用户");
    }


    @ApiOperation("根据事件id获取事件的耗时")
    @GetMapping("/time")
    public Result selectById(@RequestParam("id") int id){
        return Result.ok();
    }



}
