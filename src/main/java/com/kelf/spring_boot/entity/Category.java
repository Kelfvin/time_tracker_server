package com.kelf.spring_boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.Duration;
import java.util.List;

@TableName("category")
public class Category {

        @TableId(type = IdType.AUTO)
        private int id;
        private String name;

        private String color;

        private int userId;



        @TableField(exist = false)
        private Duration duration ; //统计分类的时间开销


        public Duration getDuration() {
                return duration;
        }

        public void setDuration(Duration duration) {
                this.duration = duration;
        }
        public int getUserId() {
                return userId;
        }

        public void setUserId(int userId) {
                this.userId = userId;
        }

        public String getColor() {
                return color;
        }

        public void setColor(String color) {
                this.color = color;
        }



        @TableField(exist = false)
        private User user;

        @TableField(exist = false)
        private List<Event> events;

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

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }

        public List<Event> getEvents() {
                return events;
        }

        public void setEvents(List<Event> events) {
                this.events = events;
        }
}
