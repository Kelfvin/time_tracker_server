package com.kelf.spring_boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class Action {

    private int id;
    private String name;

    @TableField(exist = false)
    private Category category;
    private int color;

    @TableField(exist = false)
    private User user;


}
