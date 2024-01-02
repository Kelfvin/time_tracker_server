package com.kelf.spring_boot.controller;


import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.RecordMapper;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import com.kelf.spring_boot.entity.Record;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @ApiOperation("增加记录")
    @PostMapping("/add")
    public Result createRecord(@RequestBody Map<String,Object> requestBody) throws ParseException {
        /** {
         "record": {
         "startTime": "2023-12-31T16:00:00.000+00:00",
         "endTime": "2023-12-31T16:01:00.000+00:00",
         "mark": "kelf kiss kiss",
         "userId": 1,
         "eventId": 1
         },
         "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzA0MTc0MTU0LCJleHAiOjE3MDQ3Nzg5NTR9.3VL9Z9eeOGUMDVcEGyxiXIZjdvx-kIMAU0wddIsc78c"
         }
         **/

        Map<String,Object> recordData = (Map<String, Object>) requestBody.get("record");
        Record record = new Record();

        //获得开始时间
        // 创建日期时间格式化器并设置时区
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        formatter = formatter.withZone(TimeZone.getTimeZone("Asia/Shanghai").toZoneId());
        // 解析开始时间字符串
        LocalDateTime startTime = LocalDateTime.parse((String)recordData.get("startTime"), formatter);
        record.setStartTime(startTime);

        //获得结束时间
        if (recordData.get("endTime") != null) {
            LocalDateTime endTime = LocalDateTime.parse((String) recordData.get("endTime"), formatter);
            record.setEndTime(endTime);
        }

        record.setMark((String) recordData.get("mark"));
        record.setUserId((Integer) recordData.get("userId"));
        record.setEventId((Integer) recordData.get("eventId"));
        String token = (String) requestBody.get("token");

        if(record.getUserId() == userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId()){
            recordMapper.addRecord(record);
            return Result.ok().data("record",record);
        }

        return Result.error().message("非当前登录用户").data("userId",userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId());
    }

    @ApiOperation("根据Token查询记录")
    @GetMapping("/findAll")
    public Result findAllRecord(@RequestBody Map<String,Object> requestBody){
        String token = (String) requestBody.get("token");
        User user = userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject());

        return Result.ok().data("records",recordMapper.getAllRecordByUserId(user.getId()));
    }


    @ApiOperation("删除记录")
    @DeleteMapping("/del")
    public Result deleteRecord(@RequestBody Map<String,Object> requestBody){
        int id = (Integer) requestBody.get("id");
        String token = (String) requestBody.get("token");
        if(userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId() == recordMapper.getUserIdById(id)){
            recordMapper.deleteRecord(id);
            return Result.ok().message("删除成功").data("id",id);
        }

        return Result.error().message("非当前登录用户");
    }

//    @ApiOperation("根据日期查询某一天的记录")
//    @GetMapping("/day")
//    /*
//    {
//  "startTimestamp": "2022-01-01T10:00:00",
//  "endTimestamp": "2022-01-01T12:00:00",
//  "mark": "Test",
//  "eventId": 1,
//  "userId": 1
//}
//*/
//    public List<Record> findByDate(@RequestParam("date") String date) {
//        LocalDate localDate = LocalDate.parse(date);
//        return recordMapper.findByDate(localDate);
//    }
//
//    @ApiOperation("根据日期查询某一周的记录")
//    @GetMapping("/week")
//    public List<Record> findByWeek(@RequestParam("date") String date) {
//        LocalDate localDate = LocalDate.parse(date);
//        return recordMapper.findByWeek(localDate);
//    }
//
//    @ApiOperation("根据日期查询某一月的记录")
//    @GetMapping("/month")
//    public List<Record> findByMonth(@RequestParam("date") String date) {
//        LocalDate localDate = LocalDate.parse(date);
//        return recordMapper.findByMonth(localDate);
//    }

}
