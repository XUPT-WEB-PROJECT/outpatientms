package com.xupt.outpatientms.controller;

import com.alibaba.fastjson.JSON;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.common.CurrentUserData;
import com.xupt.outpatientms.dto.MedicalRecordDTO;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.DoctorRecordService;
import com.xupt.outpatientms.util.ResponseBuilder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * Created by BorisLiu on 2020/6/3
 */
@RestController
@RequestMapping("/doctorRecord")
public class DoctorRecordController {

    @Autowired
    public DoctorRecordService doctorRecordService;

    @ApiOperation(value="医生对预约单状态的修改",
            notes = "修改成功返回true否则false")
    @RequestMapping(value = "/update/{recordId}",method =  RequestMethod.POST)
    public ResponseBuilder<Boolean> updateRecord(@PathVariable("recordId")String recordId, ServletRequest request){
        CurrentUserData data = (CurrentUserData) request.getAttribute("currentUser");
        if (data == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        boolean flag = doctorRecordService.updateRecord(recordId);
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, flag ? "修改预约单成功":"修改预约单失败，只有待就诊的状态才可以修改！");
    }

    @ApiOperation(value="医生追加诊断记录",
            notes = "追加成功返回true否则false")
    @RequestMapping(value = "/write/{doctorId}/{userId}/{recordId}",method =  RequestMethod.POST)
    public ResponseBuilder<List<Record>> writeRecord(@RequestBody String medicalRecord,@PathVariable("recordId") String recordId,@PathVariable("userId")String userId, @PathVariable("doctorId")String doctorId,ServletRequest request){
        CurrentUserData data = (CurrentUserData) request.getAttribute("currentUser");
        if (data == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        String realMedicalRecord = null;
        int i = medicalRecord.indexOf("\"conditionDescription\":\"") + "\"conditionDescription\":\"".length();
        if(i < "\"conditionDescription\":\"".length()) realMedicalRecord =  "诊断记录记录失败";
        else {
            int end = medicalRecord.indexOf(":");
            realMedicalRecord = medicalRecord.substring(i, end);
        }
        boolean flag = doctorRecordService.writeMedicalRecord(realMedicalRecord,recordId,userId,doctorId);
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, flag ? "追加诊断记录成功":"追加诊断记录失败" );
    }

    @ApiOperation(value="医生追加诊断记录", notes = "追加成功返回订单信息")
    @ApiImplicitParam(name = "medicalRecord", dataType = "application/json",
    value = "eg:" +
            "{\n" +
            "    \"recordId\": \"0000\", \n" +
            "    \"doctorId\": \"0000\", \n" +
            "    \"userId\": \"0000\", \n" +
            "    \"medicalRecord\": \"多喝热水\"\n" +
            "}")
    @RequestMapping(value = "updateMedicalRecord",method =  RequestMethod.POST)
    public ResponseBuilder<Record> updateMedicalRecord(@RequestBody MedicalRecordDTO medicalRecord, ServletRequest request){
        CurrentUserData data = (CurrentUserData) request.getAttribute("currentUser");
        if (data == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        if(!data.getId().equals(medicalRecord.getDoctorId())
            || medicalRecord.getMedicalRecord() == null
                || medicalRecord.getRecordId() == null
                || medicalRecord.getUserId() == null
        ){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "参数有误");
        }
        boolean flag = doctorRecordService.writeMedicalRecord(medicalRecord.getMedicalRecord(),
                medicalRecord.getRecordId(),
                medicalRecord.getUserId(),
                medicalRecord.getDoctorId());
        if(flag){
            Record record = doctorRecordService.getRecord(medicalRecord.getRecordId());
            return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "追加诊断记录成功", record);
        }else {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "追加诊断记录失败");
        }
    }

    @ApiOperation(value="查询该医生今日的全部就诊单",
            notes = "追加成功返回true否则false")
    @RequestMapping(value = "/list/{doctorId}/{recordDate}",method =  RequestMethod.GET)
    public ResponseBuilder<List<Record>> getTodayAllRecord(@PathVariable("doctorId") String doctorId, @PathVariable("recordDate")String recordDate, ServletRequest request){
        CurrentUserData data = (CurrentUserData) request.getAttribute("currentUser");
        if (data == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询就诊单成功", doctorRecordService.getTodayAllRecord(doctorId,recordDate));
    }
}
