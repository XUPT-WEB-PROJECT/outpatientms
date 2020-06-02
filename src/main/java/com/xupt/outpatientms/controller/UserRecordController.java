package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.UserRecordService;
import com.xupt.outpatientms.util.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userRecord")
@Api(tags = "用户端挂号相关接口")
public class UserRecordController {

    @Autowired
    private UserRecordService userRecordService;

//    @ApiOperation(value="用户登录",
//            notes = "用户登录接口，接口调用成功errCode=0，用户信息返回在data字段，token在Response Headers\"authorization\"字段中。否则错误信息返回至errMsg\n")
//    @ApiImplicitParam(name = "user", required = true, dataType = "application/json",
//            value = "用户登录\n"+
//                    "eg:\n"+
//                    "{\n" +
//                    "\t\"userTel\": \"15955897607\",\n" +
//                    "\t\"userPwd\": \"123456\"\n" +
//                    "}\n")
////    @RequestMapping(value = "login", method = RequestMethod.POST)
//    public ResponseBuilder<Record> makeRecord(){
//        return null;
//    }

    @ApiOperation(value="用户获取科室信息",
            notes = "用户获取科室信息接口，用于选择科室，接口调用成功errCode=0，科室信息返回在data字段。\n")
    @RequestMapping(value = "choseDepartment", method = RequestMethod.POST)
    public ResponseBuilder<List<Department>> choseDepartment() {
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功",userRecordService.choseDepartment());
    }
}
