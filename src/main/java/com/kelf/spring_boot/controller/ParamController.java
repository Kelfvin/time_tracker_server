package com.kelf.spring_boot.controller;

import com.kelf.spring_boot.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParamController {

    @RequestMapping(value = "/postTest1",method = RequestMethod.POST)
    public String postTest1() {
        return "postTest1";
    }

    @RequestMapping(value = "/postTest2",method = RequestMethod.POST)
    public String postTest2(String username, String password) {
        return "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
    }

    @RequestMapping(value = "/postTest3",method = RequestMethod.POST)
    public String postTest3(User user) {
        return "{\"username\":\""+user.getUsername()+"\",\"password\":\""+user.getPassword()+"\"}";
    }

    @RequestMapping(value = "/postTest4",method = RequestMethod.POST)
    public String postTest4(@RequestBody User user) {
        return "{\"username\":\""+user.getUsername()+"\",\"password\":\""+user.getPassword()+"\"}";
    }

}
