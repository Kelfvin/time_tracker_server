package com.kelf.spring_boot.mapper;


import com.kelf.spring_boot.entity.Group;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GroupMapper {

    // 根据用户id获取用户所在的组
    @Select("select * from `group` where id = (select group_id from user where id = #{userId})")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "color", column = "color"),
                    @Result(property = "users" , column = "id",
                            many = @Many(select = "com.kelf.spring_boot.mapper.UserMapper.selectByGroupId"))
            }
    )
    Group selectByUserId(int userId);



}
