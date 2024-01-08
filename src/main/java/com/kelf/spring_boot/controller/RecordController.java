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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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


    // 获取当前的记录
    @ApiOperation("获取当前的记录")
    @GetMapping("/current")
    public Result getCurrentRecord(@RequestHeader("Authorization") String token) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);

        Record record = recordMapper.selectRecordByUserIdAndEndTimestampIsNull(user.getId());

        if (record == null) {
            return Result.error().message("当前没有正在进行的记录");
        }

        // 查询事件
        record.setEvent(eventMapper.getEventById(record.getEventId()));

        return Result.ok().data("record", record);
    }

    // 获取记录
    @ApiOperation("根据id获取记录")
    @GetMapping("/getById")
    public Result getRecordById(@RequestHeader("Authorization") String token, int id) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);
        Record record = recordMapper.selectById(id);

        // 打印一下
        System.out.println(record);


        if(record == null){
            return Result.error().message("记录不存在");
        }

        if (record.getUserId() != user.getId()) {
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
        Record record = recordMapper.selectRecordByUserIdAndEndTimestampIsNull(user.getId());
        // 如果有正在进行的记录，结束它
        if (record != null) {
            record.setEndTimestamp(LocalDateTime.now());
            recordMapper.updateRecord(record);
        }

        // 开始新的记录
        record = new Record();
        record.setStartTimestamp(LocalDateTime.now());
        record.setEventId(eventId);
        record.setUserId(user.getId());
        recordMapper.addRecord(record);

        // 查询事件
        record.setEvent(eventMapper.getEventById(eventId));

        return Result.ok().message("开始记录成功").data("record", record);

    }


    // 结束某一条记录
    @ApiOperation("结束某一条记录")
    @PostMapping("/end")
    public Result endRecord(@RequestHeader("Authorization") String token, int recordId) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);

        // 查询
        Record record = recordMapper.selectById(recordId);
        if (record == null || record.getUserId() != user.getId()) {
            return Result.error().message("非当前登录用户");
        }

        // 如果是已经结束的记录，直接返回
        if (record.getEndTimestamp() != null) {
            return Result.ok().message("已经结束的记录");
        }

        // 结束记录
        record.setEndTimestamp(LocalDateTime.now());
        recordMapper.updateRecord(record);

        return Result.ok().message("结束记录成功").data("record", record);
    }

    @ApiOperation("增加记录")
    @PostMapping("/add")
    public Result createRecord(@RequestHeader("Authorization") String token, @RequestBody Record record) {
        /** {
         {
         "startTimestamp": "2024-01-08T08:15:00.000",
         "endTimestamp": "2024-01-08T10:20:00.000",
         "mark": "kelf kiss kiss",
         "eventId": 1
         }
         **/

        // 查询用户
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        User user = userMapper.selectByUsername(username);

        // 查询事件
        Event event = eventMapper.getEventById(record.getEventId());
        if (event == null || event.getUserId() != user.getId()) {
            return Result.error().message("非当前登录用户");
        }

        // 查询与该记录时间有重叠的记录
        List<Record> records = recordMapper.selectRecordByUserIdAndConflictTimeRange(user.getId(), record.getStartTimestamp(), record.getEndTimestamp());
        // 遍历每一条记录，如果有重叠的进行处理
        // 特别的，如果记录是正在进行的记录，如果本次我们添加的记录结束时间大于正在进行的记录的开始时间，那么就报错
        // 1. 若该记录的开始时间和结束时间都在本次记录的时间范围内，则删除该记录
        // 2. 若该记录的开始时间在本次记录开始时间之前，结束时间在本次记录结束时间之后，则将该记录的结束时间改为本次记录的开始时间，当然要考虑这个长短是多少，如果长度小于一个范围，就删除该记录
        // 3. 若该记录的开始时间在本次记录开始时间之后，结束时间在本次记录结束时间之前，则将该记录的开始时间改为本次记录的结束时间，当然要考虑这个长短是多少，如果长度小于一个范围，就删除该记录

        LocalDateTime newRecordStartTime = record.getStartTimestamp();
        LocalDateTime newRecordEndTime = record.getEndTimestamp();

        for(var oldRecord : records) {
            LocalDateTime oldRecordStartTime = oldRecord.getStartTimestamp();
            LocalDateTime oldRecordEndTime = oldRecord.getEndTimestamp();


            // 如果是正在进行的记录，那么就要判断一下
            if (oldRecordEndTime == null) {
                return Result.error().message("已经有正在进行的记录");
            }

            // 如果是已经结束的记录，那么就要判断一下

                // 如果本次我们添加的记录的开始时间和结束时间都在该记录的时间范围内，那么就删除该记录
            // 改为不早于和不晚于，这样可以避免一些边界问题
            if(!oldRecordStartTime.isBefore(newRecordStartTime)  && !oldRecordEndTime.isAfter(newRecordEndTime)){
                recordMapper.deleteRecord(oldRecord.getId());
                continue;
            }


                // 如果原来的记录的时长覆盖了本次记录的时长，那么就要对原来的记录进行处理，对原来的记录进行分割
            if (!oldRecordStartTime.isAfter(newRecordStartTime) && !oldRecordEndTime.isBefore(newRecordEndTime)) {
                // 尝试进行切割，前面的保留，增加一条记录
                Record recordTemp2 = new Record();
                // 计算看一下后面的记录的长度是否大于一个范围
                if (Duration.between(newRecordEndTime, oldRecordEndTime).toSeconds() > 5) {
                    recordTemp2.setStartTimestamp(newRecordEndTime);
                    recordTemp2.setEndTimestamp(oldRecordEndTime);
                    recordTemp2.setEventId(oldRecord.getEventId());
                    recordTemp2.setUserId(oldRecord.getUserId());
                    recordMapper.addRecord(recordTemp2);
                }

                // 看前面的时间范围
                if (Duration.between(oldRecordStartTime, newRecordStartTime).toSeconds() > 5) {
                    oldRecord.setEndTimestamp(newRecordStartTime);
                    recordMapper.updateRecord(oldRecord);
                } else {
                    recordMapper.deleteRecord(oldRecord.getId());
                }

            }



            // 如果本次我们添加的记录的开始时间在该记录的开始时间之前，结束时间在该记录的结束时间之前
            // 就把原来的记录的开始时间改为本次记录的结束时间
                if (!newRecordStartTime.isAfter(oldRecordStartTime) && !newRecordEndTime.isAfter(oldRecordEndTime)) {
                    if (Duration.between(newRecordEndTime, oldRecordEndTime).toSeconds() < 5) {
                        recordMapper.deleteRecord(oldRecord.getId());
                    } else {
                        oldRecord.setStartTimestamp(newRecordEndTime);
                        recordMapper.updateRecord(oldRecord);
                    }
                    continue;
                }

                // 如果本次我们添加的记录的开始时间在该记录的开始时间之后，结束时间在该记录的结束时间之后
            // 就把原来的记录的结束时间改为本次记录的开始时间
            // 如果更改后长度小于一个范围，就删除该记录
                if (!newRecordStartTime.isBefore(oldRecordStartTime) && !newRecordEndTime.isBefore(oldRecordEndTime)) {
                    if ( Duration.between(oldRecordStartTime, newRecordStartTime).toSeconds() < 5) {
                        recordMapper.deleteRecord(oldRecord.getId());
                    } else {
                        oldRecord.setEndTimestamp(newRecordStartTime);
                        recordMapper.updateRecord(oldRecord);
                    }
                }

        }

        // 添加记录
        record.setUserId(user.getId());
        recordMapper.addRecord(record);

        // 查询事件
        record.setEvent(eventMapper.getEventById(record.getEventId()));

        return Result.ok().message("添加记录成功").data("record", record);

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
        "date":"2024-01-01",
        */
    public Result findByDate( @RequestHeader("Authorization") String token ,String date){
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
