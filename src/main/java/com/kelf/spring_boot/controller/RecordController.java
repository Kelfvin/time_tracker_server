package com.kelf.spring_boot.controller;


import com.kelf.spring_boot.entity.Event;
import com.kelf.spring_boot.entity.User;
import com.kelf.spring_boot.mapper.EventMapper;
import com.kelf.spring_boot.mapper.RecordMapper;
import com.kelf.spring_boot.mapper.UserMapper;
import com.kelf.spring_boot.utils.JwtUtils;
import com.kelf.spring_boot.utils.Result;
import com.kelf.spring_boot.entity.Record;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventMapper eventMapper;

    // 获取记录
    @ApiOperation("根据id获取记录")
    @GetMapping("/getById")
    public Result getRecordById(@RequestHeader("Authorization") String token, int id) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        Record record = recordMapper.getRecordById(id);
        if (record == null || record.getUserId() != user.getId()) {
            return Result.error().message("非当前登录用户");
        }
        return Result.ok().data("record", record);
    }

    // 开始某一条记录
    @ApiOperation("开始某一条记录")
    @PostMapping("/start")
    public Result startRecord(@RequestHeader("Authorization") String token, int eventId) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);

        // 查询事件
        Event event = eventMapper.getEventById(eventId);
        if (event == null || event.getUserId() != user.getId()) {
            return Result.error().message("非当前登录用户");
        }

        // 查询当前用户正在进行的记录
        Record record = recordMapper.getRecordByUserIdAndEndTimeStampIsNull(user.getId());
        // 如果有正在进行的记录，结束它
        if (record != null) {
            record.setEndTimeStamp(LocalDateTime.now());
            recordMapper.updateRecord(record);
        }

        // 开始新的记录
        record = new Record();
        record.setStartTimeStamp(LocalDateTime.now());
        record.setEventId(eventId);
        record.setUserId(user.getId());
        recordMapper.addRecord(record);

        return Result.ok().message("开始记录成功");

    }


    // 结束某一条记录
    @ApiOperation("结束某一条记录")
    @PostMapping("/end")
    public Result endRecord(@RequestHeader("Authorization") String token, int recordId) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);

        // 查询
        Record record = recordMapper.getRecordById(recordId);
        if (record == null || record.getUserId() != user.getId()) {
            return Result.error().message("非当前登录用户");
        }

        // 如果是已经结束的记录，直接返回
        if (record.getEndTimeStamp() != null) {
            return Result.ok().message("已经结束的记录");
        }

        // 结束记录
        record.setEndTimeStamp(LocalDateTime.now());
        recordMapper.updateRecord(record);

        return Result.ok().message("结束记录成功");
    }

    @ApiOperation("增加记录")
    @PostMapping("/add")
    public Result createRecord(@RequestBody Map<String,Object> requestBody) throws ParseException {
        /** {
         "record": {
         "startTimeStamp": "2023-12-31T16:00:00.000+00:00",
         "endTimeStamp": "2023-12-31T16:01:00.000+00:00",
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
        LocalDateTime startTimeStamp = LocalDateTime.parse((String)recordData.get("startTimeStamp"), formatter);
        record.setStartTimeStamp(startTimeStamp);

        //获得结束时间
        if (recordData.get("endTimeStamp") != null) {
            LocalDateTime endTimeStamp = LocalDateTime.parse((String) recordData.get("endTimeStamp"), formatter);
            record.setEndTimeStamp(endTimeStamp);
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

    @ApiOperation("根据日期查询某一天的记录")
    @GetMapping("/day")
    /*
        {
        "date":"2024-01-01",
         "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzA0MTc0MTU0LCJleHAiOjE3MDQ3Nzg5NTR9.3VL9Z9eeOGUMDVcEGyxiXIZjdvx-kIMAU0wddIsc78c"
        }

*/
    public Result findByDate(@RequestBody Map<String,Object> requestBody){
        String date = (String) requestBody.get("date");
        String token = (String) requestBody.get("token");
        LocalDate localDate = LocalDate.parse(date);
        int userId = userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId();

        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        List<Record> records = recordMapper.findByDateTimeRange(startOfDay, endOfDay, userId);

//        System.out.println(startOfDay);
//        System.out.println(endOfDay);

        return Result.ok().data("recordOfDay", records);
    }



    @ApiOperation("根据日期查询某一周的记录")
    @GetMapping("/week")
    // 查询某一周的记录
    public Result findByWeek(@RequestBody Map<String, Object> requestBody) {
        /**
         * {
         *         "startDate": "2023-12-18",
         *         "endDate": "2023-12-24",
         *          "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzA0MTc0MTU0LCJleHAiOjE3MDQ3Nzg5NTR9.3VL9Z9eeOGUMDVcEGyxiXIZjdvx-kIMAU0wddIsc78c"
         *}
         */
        String startDate = (String) requestBody.get("startDate");
        String endDate = (String) requestBody.get("endDate");
        String token = (String) requestBody.get("token");
        int userId = userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId();

        LocalDateTime startOfWeek = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime endOfWeek = LocalDate.parse(endDate).atTime(LocalTime.MAX);

        System.out.println(startOfWeek);
        System.out.println(endOfWeek);

        return Result.ok().data("recordsOfWeek", recordMapper.findByWeek(startOfWeek, endOfWeek, userId));
    }

    @ApiOperation("根据日期查询某一月的记录")
    @GetMapping("/month")
    // 查询某一月的记录
    public Result findByMonth(@RequestBody Map<String, Object> requestBody) {
        /**
         * {
         *   "year": "2024",
         *   "month": "1",
         *   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzA0MTc0MTU0LCJleHAiOjE3MDQ3Nzg5NTR9.3VL9Z9eeOGUMDVcEGyxiXIZjdvx-kIMAU0wddIsc78c"
         * }
         */
        String year = (String) requestBody.get("year");
        String month = (String) requestBody.get("month");
        String token = (String) requestBody.get("token");
        int userId = userMapper.selectByUsername(JwtUtils.getClaimsByToken(token).getSubject()).getId();

        LocalDateTime startOfMonth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1)
                .plusMonths(1)
                .atStartOfDay()
                .minusSeconds(1);


        System.out.println(startOfMonth);
        System.out.println(endOfMonth);

        return Result.ok().data("recordsOfMonth", recordMapper.findByMonth(startOfMonth, endOfMonth, userId));
    }

}
