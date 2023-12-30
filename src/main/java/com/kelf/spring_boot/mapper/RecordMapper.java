package com.kelf.spring_boot.mapper;


import com.kelf.spring_boot.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper

public interface RecordMapper {

    @Select("select * from record where user_id = #{userId}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "startTime", column = "start_timestamp"),
                    @Result(property = "endTime", column = "end_timestamp"),
                    @Result(property = "action", column = "action_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.EventMapper.selectById")),
                    @Result(property = "user", column = "user_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.UserMapper.selectById"))
            }
    )
    public List<Record> getAllRecordByUserId(int userId);


    // 获取某一天的记录
    @Select("select * from record where user_id = #{userId} and date(start_timestamp) = #{date}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "startTime", column = "start_timestamp"),
                    @Result(property = "endTime", column = "end_timestamp"),
                    @Result(property = "action", column = "action_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.EventMapper.selectById")),
                    @Result(property = "user", column = "user_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.UserMapper.selectById"))
            }
    )
    public List<Record> getRecordByDate(int userId, String date);




}
