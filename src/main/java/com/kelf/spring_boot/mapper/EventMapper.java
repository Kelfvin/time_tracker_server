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
                    @Result(property = "category", column = "category_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.CategoryMapper.selectById")),
                    @Result(property = "user", column = "user_id",
                            one = @One(select = "com.kelf.spring_boot.mapper.UserMapper.selectById"))
            }
    )
    List<Event> selectByCategoryId(int categoryId);

//    //获取所有事件
//    @Select("SELECT * FROM event")
//    List<Event> getAllEvents();

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




}
