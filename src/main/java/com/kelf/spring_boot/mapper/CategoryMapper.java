package com.kelf.spring_boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kelf.spring_boot.entity.Category;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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
                            many = @Many(select = "com.kelf.spring_boot.mapper.ActionMapper.selectByCategoryId"))
            }
    )
    public List<Category> getAllCategoryByUserId(int userId);


}
