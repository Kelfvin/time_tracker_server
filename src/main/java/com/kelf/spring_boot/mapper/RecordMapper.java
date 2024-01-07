package com.kelf.spring_boot.mapper;


import com.kelf.spring_boot.entity.Record;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper

public interface RecordMapper {

    @Select("select * from record where user_id = #{userId}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "startTime", column = "start_timestamp"),
                    @Result(property = "endTime", column = "end_timestamp"),
                    @Result(property = "eventId", column = "event_id"),
                    @Result(property = "userId", column = "user_id")
            }
    )
    List<Record> getAllRecordByUserId(int userId);


    // 获取某一天的记录
    @Select("select * from record where user_id = #{userId} and date(start_timestamp) = #{date}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "startTimestamp", column = "start_timestamp"),
                    @Result(property = "endTimestamp", column = "end_timestamp"),
                    @Result(property = "event", column = "event_id",
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


    @Select("select * from record where user_id = #{id}")
    List<Record> getRecordByUserId(int id);

    @Select("select user_id from record where id = #{id}")
    int getUserIdById(int id);

//    //查询某一天的记录
//    @Select("SELECT * FROM record WHERE start_timestamp >= #{startOfDay} AND end_timestamp <= #{endOfDay} AND user_id = #{userId}")
//    List<Record> findByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay, @Param("userId") int userId);

    //查询某一天的所有记录
    @Select("SELECT * FROM record WHERE ((start_timestamp >= #{startOfDay} AND end_timestamp <= #{endOfDay}) OR (start_timestamp >= #{startOfDay} AND start_timestamp <= #{endOfDay})) AND user_id = #{userId}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "startTimestamp", column = "start_timestamp"),
                    @Result(property = "endTimestamp", column = "end_timestamp"),
                    @Result(property = "event", column = "event_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.EventMapper.selectById")),
                    @Result(property = "user", column = "user_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.UserMapper.selectById"))
            }
    )
    List<Record> findByDateTimeRange(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay, @Param("userId") int userId);

    // 查询某一周的记录
    @Select("SELECT * FROM record WHERE (start_timestamp >= #{startDate} AND end_timestamp <= #{endDate}) " +
            "OR (start_timestamp >= #{startDate} AND start_timestamp <= #{endDate}) AND user_id = #{userId}")
    List<Record> findByWeek(@Param("startDate") LocalDateTime startOfWeek, @Param("endDate") LocalDateTime endOfWeek, @Param("userId") int userId);

    // 查询某一月的记录
    @Select("SELECT * FROM record WHERE start_timestamp >= #{startOfMonth} AND end_timestamp <= #{endOfMonth} " +
            "AND user_id = #{userId}")
    List<Record> findByMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth, @Param("userId") int userId);

    // 根据id查询记录
    @Select("SELECT * FROM record WHERE id = #{id}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "startTimestamp", column = "start_timestamp"),
                    @Result(property = "endTimestamp", column = "end_timestamp"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "eventId", column = "event_id"),
                    @Result(property = "event", column = "event_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.EventMapper.selectById")),
                    @Result(property = "user", column = "user_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.UserMapper.selectById"))
            }
    )
    Record selectById(int id);

    // 根据用户id查询记录，且结束时间为空
    @Select("SELECT * FROM record WHERE user_id = #{id} AND end_timestamp IS NULL")
    Record selectRecordByUserIdAndEndTimestampIsNull(int id);
}
