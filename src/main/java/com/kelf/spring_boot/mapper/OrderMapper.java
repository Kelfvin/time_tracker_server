package com.kelf.spring_boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kelf.spring_boot.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper

public interface OrderMapper extends BaseMapper<Order>  {

    @Select("select * from orders where uid = #{userId}")
    Order selectByUserId(int userId);

    @Select("select * from orders")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "user", column = "uid", one = @One(select = "com.kelf.spring_boot.mapper.UserMapper.selectById"))

            }
    )
    List<Order> getAllOrder();


    @Select("select * from orders")
    List<Order> getOrdersByUserName(String userName);

}
