package com.kelf.spring_boot.mapper;

import com.kelf.spring_boot.entity.Tag;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TagMapper {

    //获取所有Tag
    @Select("SELECT * FROM tag")
    List<Tag> getAllTags();

    //根据id获取Tag
    @Select("SELECT * FROM tag WHERE id = #{id}")
    Tag getTagById(int id);

    //增加Tag
    @Insert("INSERT INTO tag(name, color) VALUES(#{name}, #{color})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addTag(Tag tag);

    //更新Tag
    @Update("UPDATE tag SET name = #{name}, color = #{color} WHERE id = #{id}")
    void updateTag(Tag tag);

    //删除Tag
    @Delete("DELETE FROM tag WHERE id = #{id}")
    void deleteTag(int id);
}
