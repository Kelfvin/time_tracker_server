package com.kelf.spring_boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

public class Group {
    private int id;
    private String name;


    @TableField(exist = false) // 不是数据库字段,要通过User表中的group_id字段来查找
    List<User> users;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
