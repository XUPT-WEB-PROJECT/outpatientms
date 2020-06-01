package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.service.UserRecordService;
import com.xupt.outpatientms.util.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户端挂号相关接口")
@RestController
@RequestMapping("/userRecord")
public class UserRecordController {

    @Autowired
    private UserRecordService userRecordService;

    @ApiOperation(value="用户登录",
            notes = "用户登录接口，接口调用成功errCode=0，用户信息返回在data字段，token在Response Headers\"authorization\"字段中。否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "user", required = true, dataType = "application/json",
            value = "用户登录\n"+
                    "eg:\n"+
                    "{\n" +
                    "\t\"userTel\": \"15955897607\",\n" +
                    "\t\"userPwd\": \"123456\"\n" +
                    "}\n")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseBuilder<Record> makeRecord(){
        return null;
    }
}