package com.kelf.spring_boot.controller;


import com.kelf.spring_boot.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/order/findAll")
    public String getAllOrder() {
        return orderMapper.getAllOrder().toString();
    }

    @GetMapping("/order/findByUserName")
    public String getOrderByUserName(String userName) {
        return orderMapper.getOrdersByUserName(userName).toString();
    }



}
