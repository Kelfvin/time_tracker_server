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
    public Result addEvent(@RequestBody Map<String, Object> requestBody){
        /** 测试json格式
         {
         "event":{
         "name":"打人",
         "color":"#ffffff",
         "categoryId":1,
         "userId":1
         },
         "token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzA0MTc0MTU0LCJleHAiOjE3MDQ3Nzg5NTR9.3VL9Z9eeOGUMDVcEGyxiXIZjdvx-kIMAU0wddIsc78c"
         }
         **/
        Map<String,Object> eventMap = (Map<String,Object>) requestBody.get("event");
        Event event = new Event();
        event.setName((String) eventMap.get("name"));
        event.setColor((String) eventMap.get("color"));
        event.setCategoryId((Integer) eventMap.get("categoryId"));
        event.setUserId((Integer) eventMap.get("userId"));
        String token = (String) requestBody.get("token");
        if(event.getUserId()== userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId()){
            eventMapper.addEvent(event);
            return Result.ok().message("增加事件成功");
        }

        return Result.error().message("非登录用户");
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
    @DeleteMapping("/del")
    public Result deleteEvent(@RequestBody Map<String, Object> requestBody){
        int eventId = (Integer) requestBody.get("eventId");
        String token = (String) requestBody.get("token");
        //如果用户id和token的一致，则删除事件
        if(userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId() == eventMapper.getUserIdByEventId(eventId)){
            eventMapper.deleteEvent(eventId);
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




}
