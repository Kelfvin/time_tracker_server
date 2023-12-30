package com.kelf.spring_boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class Action {

    private int id;
    private String name;


    int color;

    private int categoryId;

    private int userId;

    @TableField(exist = false)
    private Category category;


    @TableField(exist = false)
    private User user;


}
