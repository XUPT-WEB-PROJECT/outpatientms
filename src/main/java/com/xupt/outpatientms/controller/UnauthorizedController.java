package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UnauthorizedController {

    @RequestMapping(value = "/unauthorized")
    public ResponseBuilder<Object> unauthorized(HttpServletResponse response){
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return new ResponseBuilder<>(ErrCodeEnum.ERR_NOTLOGIN, "登录信息有误");
    }

}
