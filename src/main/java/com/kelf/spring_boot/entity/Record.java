package com.kelf.spring_boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("record")
public class Record {
    @TableId(type = IdType.AUTO)
    private int id;
    private LocalDateTime startTimeStamp;
    private LocalDateTime endTimeStamp;
    private String mark = null;

    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    private int eventId;
    @TableField(exist = false)
    private Event event;

    @TableField(exist = false)
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(LocalDateTime startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public LocalDateTime getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(LocalDateTime endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
