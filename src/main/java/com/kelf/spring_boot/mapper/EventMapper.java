package com.kelf.spring_boot.mapper;


import com.kelf.spring_boot.entity.Event;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EventMapper {

    @Select("select * from event where id = #{id}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "categoryId", column = "category_id"),
                    @Result(property = "color", column = "color"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "category", column = "category_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.CategoryMapper.selectById")),
                    @Result(property = "user", column = "user_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.UserMapper.selectById"))
            }
    )
    Event selectById(int id);

    @Select("select * from event where category_id = #{categoryId}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "color", column = "color"),
                    @Result(property = "categoryId", column = "category_id"),
                    @Result(property = "userId", column = "user_id")
            }
    )
    List<Event> selectByCategoryId(int categoryId);

    //根据用户名username获取所有事件
    @Select("select * from event where user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "color", column = "color"),
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "userId", column = "user_id")
    })
    List<Event> getEventsByUserId(int userId);

    //增加事件
    @Insert("INSERT INTO event (name, color, category_id, user_id) " +
            "VALUES (#{event.name}, #{event.color}, #{event.categoryId}, #{event.userId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "event.id")
    void addEvent(@Param("event") Event event);

    //更新事件
    @Update("UPDATE event SET name = #{name}, color = #{color}, category_id = #{categoryId}, user_id = #{userId} WHERE id = #{id}")
    void updateEvent(Event event);

    //删除事件
    @Delete("DELETE FROM event WHERE id = #{id}")
    void deleteEvent(int id);


    //根据事件id获得事件的用户id
    @Select("select user_id from event where id = #{eventId}")
    int getUserIdByEventId(int eventId);

    //根据事件id获得事件
    @Select("select * from event where id = #{eventId}")
    Event getEventById(int eventId);


}
