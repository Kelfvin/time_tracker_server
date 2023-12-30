package com.kelf.spring_boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kelf.spring_boot.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 获取用户的所有分类
    @Select("select * from category where id = #{userId}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "color", column = "color"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "actions", column = "id",
                            many = @Many(select = "com.kelf.spring_boot.mapper.EventMapper.selectByCategoryId"))
            }
    )
    List<Category> getAllCategoryByUserId(int userId);


    // 增加分类
    @Insert("insert into category (name, color, user_id) values (#{name}, #{color}, #{user_id})")
    void addCategory(int user_id, Category category);

}
