package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.UserRecordService;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.vo.UserChoseDoctorVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/userRecord")
@Api(tags = "用户端挂号相关接口")
public class UserRecordController {

    @Autowired
    private UserRecordService userRecordService;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
    @RequestMapping(value = "choseDepartment", method = RequestMethod.GET)
    public ResponseBuilder<List<Department>> choseDepartment() {
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功",userRecordService.choseDepartment());
    }

    @ApiOperation(value="用户查询预约医生",
            notes = "用户按科室、日期查询医生信息接口，仅返回当日值班医生，接口调用成功errCode=0，医生信息返回在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentName", value = "预约科室"),
            @ApiImplicitParam(name = "date", value = "预约日期，yyyy-mm-dd格式，范围为当日起一周内，每日17:30后不可预约当日门诊")
    })
    @RequestMapping(value = "/choseDoctor", method = RequestMethod.POST)
    public ResponseBuilder<List<UserChoseDoctorVO>> choseDoctor(String departmentName, String date) {
        if(StringUtils.isEmpty(departmentName)
            || StringUtils.isEmpty(date)
        ){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "参数不能为空");
        }
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Date future = calendar.getTime();
        future.setTime(future.getTime() + 7 * 86400 * 1000);
        String nowDate = now.toString();
        String futureDate = future.toString();
        Date objDate = null;
        try {
            objDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "预约日期有误或格式错误");
        }
        if(nowDate.compareTo(date) > 0  || date.compareTo(futureDate) > 0){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "预约日期不在有效范围内");
        }
        if(nowDate.equals(date)
                && (calendar.get(Calendar.HOUR_OF_DAY) > 17
                    || calendar.get(Calendar.MINUTE) > 30)
        ){//预约本日
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "每日17:30后不可预约当日门诊");
        }
        calendar.setTime(objDate);
        List<UserChoseDoctorVO> doctors = userRecordService.choseDoctor(departmentName, ((calendar.get(Calendar.DAY_OF_WEEK) + 5)%7)+1);
        for(UserChoseDoctorVO doctor : doctors){
            
        }
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功",userRecordService.choseDoctor(departmentName, 0));
    }

//    @ApiOperation(value="用户查询自己的预约记录",
//            notes = "用户按科室、日期查询医生信息接口，仅返回当日值班医生，接口调用成功errCode=0，医生信息返回在data字段，否则错误信息返回至errMsg\n")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "departmentName", value = "预约科室"),
//            @ApiImplicitParam(name = "date", value = "预约日期，yyyy-mm-dd格式，范围为当日起一周内，每日17:30后不可预约当日门诊")
//    })
//    @RequestMapping(value = "listRecord", method = RequestMethod.POST)
//    public ResponseBuilder<List<Record>> listRecord(){
//
//    }
}
