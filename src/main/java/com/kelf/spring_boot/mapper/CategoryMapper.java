package com.kelf.spring_boot.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kelf.spring_boot.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;



@Mapper
public interface CategoryMapper {
    // 获取用户的所有分类
    @Select("select * from category where user_id = #{userId}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "color", column = "color"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "events", column = "id",
                            many = @Many(select = "com.kelf.spring_boot.mapper.EventMapper.selectByCategoryId"))
            }
    )
    List<Category> getAllCategoryByUserId(int userId);


    // 增加分类
    @Insert("insert into category (name,color,user_id) values (#{name}, #{color}, #{userId})")
    void addCategory(Category category);


    //获得分类
    @Select("SELECT * FROM category WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "color", column = "color"),
//            @Result(property = "userId", column = "user_id"),
            @Result(property = "userId" , column = "user_id"),
            @Result(property = "events", column = "id",
                    many = @Many(select = "com.kelf.spring_boot.mapper.EventMapper.selectByCategoryId"))
    })
    Category getCategoryById(int id);

    //更新分类
    @Update("UPDATE category SET name = #{name}, color = #{color}, user_id = #{userId} WHERE id = #{id}")
    void updateCategory(Category category);

    //删除分类
    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteCategory(int id);

}
