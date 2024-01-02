package com.kelf.spring_boot.mapper;


import com.kelf.spring_boot.entity.Record;
import org.apache.ibatis.annotations.*;

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


    //增加Record
    @Insert("INSERT INTO record(start_timestamp, end_timestamp, mark, event_id, user_id) VALUES(#{startTimestamp}, #{endTimestamp}, #{mark}, #{eventId}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addRecord(Record record);

    // 更新Record
    @Update("UPDATE record SET start_timestamp = #{startTimestamp}, end_timestamp = #{endTimestamp}, mark = #{mark}, event_id = #{eventId}, user_id = #{userId} WHERE id = #{id}")
    void updateRecord(Record record);

    //删除Record
    @Delete("DELETE FROM record WHERE id = #{id}")
    void deleteRecord(int id);


}
