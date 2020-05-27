package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value="test",notes = "testSwagger")
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test(){
        return "hello";
    }

    @ApiOperation(value="register",notes = "患者注册")
    @ApiImplicitParam(name = "user", value = "用户注册信息",
                        dataType = "application/json", required = true)
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@RequestBody User user, HttpServletResponse response){
        userService.addUser(user);
        return "";
    }

}
