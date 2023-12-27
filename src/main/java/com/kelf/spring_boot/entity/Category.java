package com.kelf.spring_boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

public class Category {

        private int id;
        private String name;

        @TableField(exist = false)
        private User user;

        @TableField(exist = false)
        private List<Action> actions;
}
