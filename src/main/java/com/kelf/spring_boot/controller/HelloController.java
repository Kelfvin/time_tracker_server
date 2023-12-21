package com.kelf.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController    {


    @RequestMapping("/hello")
    // localhost:8080/hello?nickname=Kelf&phone=123456
    public String hello(@RequestParam("nickname") String name, int phone) {
        return "Hello "+name+"! 1111phone number is "+phone+".";
    }

    @GetMapping("/error")
    public String error(){
        return "Error!";
    }




}
