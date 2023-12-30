package com.kelf.spring_boot.controller;

import com.kelf.spring_boot.entity.Event;
import com.kelf.spring_boot.mapper.EventMapper;
import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private EventMapper eventMapper;
    @ApiOperation("增加事件")
    @PostMapping("")
    public Result createEvent(){
        return Result.ok();
    }

    @ApiOperation("查询所有事件")
    @GetMapping("/findAll")
    public List<Event> getAllEvent() {

        return new ArrayList<>();
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
