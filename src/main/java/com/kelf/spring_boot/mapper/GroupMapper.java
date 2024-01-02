package com.kelf.spring_boot.mapper;


import com.kelf.spring_boot.entity.Group;
import org.apache.ibatis.annotations.*;

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

    //根据Id获取Group
    @Select("SELECT * FROM group WHERE id = #{id}")
    Group getGroupById(int id);

    //添加分组
    @Insert("INSERT INTO group(id, name) VALUES(#{id}, #{name})")
    void addGroup(Group group);

    //更新分组
    @Update("UPDATE group SET name = #{name} WHERE id = #{id}")
    void updateGroup(Group group);

    //删除分组
    @Delete("DELETE FROM group WHERE id = #{id}")
    void deleteGroup(int id);



}
