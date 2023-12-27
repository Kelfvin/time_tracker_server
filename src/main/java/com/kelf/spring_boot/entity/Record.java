package com.kelf.spring_boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

public class Record {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String mark;

    @TableField(exist = false)
    private Action action;

    @TableField(exist = false)
    private User user;
}
