package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.service.UserRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户端挂号相关接口")
@RestController
@RequestMapping("/userRecord")
public class UserRecordController {

    @Autowired
    private UserRecordService userRecordService;


}
