package com.kelf.spring_boot.mapper;


import com.kelf.spring_boot.entity.Event;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EventMapper {

    @Select("select * from action where id = #{id}")
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

    @Select("select * from action where category_id = #{categoryId}")
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


}
