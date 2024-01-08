package com.kelf.spring_boot.controller;



import com.kelf.spring_boot.entity.Category;
import com.kelf.spring_boot.entity.Event;
import com.kelf.spring_boot.entity.Record;
import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.CategoryMapper;
import com.kelf.spring_boot.mapper.EventMapper;
import com.kelf.spring_boot.mapper.RecordMapper;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private  CategoryMapper categoryMapper;
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private RecordMapper recordMapper;

    // 增加分类
    @ApiOperation("根据传入的category和token增加分类")
    @PostMapping("")
    public Result add(@RequestHeader("Authorization") String token, @RequestBody Category category) {
        // 获取用户id
        String username = JwtUtils.getClaimsByToken(token).getSubject();


        User user = userMapper.selectByUsername(username);
        category.setUserId(user.getId());
        categoryMapper.addCategory(category);

        return Result.ok().message("增加分类成功").data("category",category);

    }



    @ApiOperation("根据id删除分类")
    @DeleteMapping("")
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
    @GetMapping("/statistics")
    public Result getStatistics(@RequestHeader("Authorization") String token, String startDate, String endDate) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        // 获取用户的分类
        List<Category> categories = categoryMapper.getAllCategoryByUserId(user.getId());
        // 获取用户的事件
        List<Event> events = eventMapper.getEventsByUserId(user.getId());
        // 获取用户的记录
        /** 先把String类型转换为LocalDateTime类型 作类型兼容 **/
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDateObject = LocalDate.parse(startDate);
        LocalDate endDateObject = LocalDate.parse(endDate);

        LocalDateTime start = startDateObject.atStartOfDay();
        // 截止日期的最后一刻
        LocalDateTime end = endDateObject.atTime(LocalTime.MAX);

//        List<Record> records = recordMapper.findByWeek(start, end,user.getId());//这里用week方法是因为传的都是起始和中止时间，效果一样

        //统计每一个事件的时间开销
        for(var event : events){
            Duration duration = Duration.ZERO; //每一个事件的时间片
            List<Record> records = recordMapper.findRecordOfCategory(user.getId(),event.getId(),start,end);
            for(Record record : records){
                // 如果是和Event相关的记录，那么就要计算时间开销
                if(record.getEventId() == event.getId()) {
                    // 如果结束时间为空，那么就用当前时间
                    if (record.getEndTimestamp() == null) {
                        record.setEndTimestamp(LocalDateTime.now());
                    }


                    // 根据时间进行计算，如果开始时间段和当前时间段是冲突的，要进行截断
                    if (record.getStartTimestamp().isBefore(start)) {
                        record.setStartTimestamp(start);
                    }

                    if (record.getEndTimestamp().isAfter(end)) {
                        record.setEndTimestamp(end);
                    }

                    Duration durationTemp = Duration.between(record.getStartTimestamp(), record.getEndTimestamp());
                    duration = duration.plus(durationTemp); //遍历累加每一个record的时间开销

                }
            }

            event.setDuration(duration);
        }

        //统计每个分类的时间开销
        for(var category : categories){
            Duration duration = Duration.ZERO; //每一个分类的时间片
            List<Event>eventList = new ArrayList<Event>();
            for(Event event:events){
                if(category.getId() == event.getCategoryId()){
                    //筛选每个分类的所有事件时间开销
                    eventList.add(event);
                    duration = duration.plus(event.getDuration());

                }
            }

            category.setDuration(duration);
            category.setEvents(eventList);
        }
        return  Result.ok().data("categories",categories).data("events",events);
    }


}
