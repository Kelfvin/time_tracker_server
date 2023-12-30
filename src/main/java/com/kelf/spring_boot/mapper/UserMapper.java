package com.kelf.spring_boot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kelf.spring_boot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


// 加上mapper注解，才能被spring扫描到
@Mapper
public interface UserMapper
{
    @Select("select * from user where username = #{username}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "avatar", column = "avatar"),
            }
    )
    User selectByUsername(String username);

    @Select("select * from user where id = #{id}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "avatar", column = "avatar"),

            }
    )
    User selectById(int id);


    @Select("select * from user")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "avatar", column = "avatar"),
            }
    )
    List<User> getAllUser();

    @Select("select * from user where group_id = #{groupId}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "avatar", column = "avatar"),
            }
    )
    List<User> selectByGroupId(int groupId);


    @Insert("insert into user(username, password, avatar) values(#{username}, #{password}, #{avatar})")
    void insert(User user);
}
