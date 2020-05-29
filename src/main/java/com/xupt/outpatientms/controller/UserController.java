package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.UserService;
import com.xupt.outpatientms.util.ResponseBuilder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value="register",notes = "用户注册接口,注册成功errCode=0，否则错误信息返回至errMsg")
    @ApiImplicitParam(name = "user",dataType = "application/json", required = true,paramType = "application/json",
            value = "用户注册信息\nuserName, userTel, userPwd为必选项\n"+
                    "userGender,userAge为可选项\n"+
                    "eg:\n"+
                        "{\n" +
                        "\t\"userName\": \"kafm\",\n" +
                        "\t\"userTel\": \"15955897607\",\n" +
                        "\t\"userPwd\": \"123456\"\n" +
                        "}")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@Validated @RequestBody User user, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage()).build();
        }
        int re = -1;
        ResponseBuilder rb = new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS,"注册成功");
        try {
            re = userService.addUser(user);
            if(re != 1){
                rb = new ResponseBuilder(ErrCodeEnum.ERR_FAILED);
            }
        }catch (DataAccessException e){
            rb = new ResponseBuilder(ErrCodeEnum.ERR_FAILED,"该电话号码已被注册");
        }
        return rb.build();
    }

}
