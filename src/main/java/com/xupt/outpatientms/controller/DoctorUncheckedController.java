package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.DoctorUnchecked;
import com.xupt.outpatientms.component.SmsComponent;
import com.xupt.outpatientms.dto.DoctorUncheckedDTO;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.DoctorUncheckedService;
import com.xupt.outpatientms.util.ResponseBuilder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by BorisLiu on 2020/5/31
 */
@RestController
@RequestMapping("/doctorUnchecked")
public class DoctorUncheckedController {

    @Autowired
    private DoctorUncheckedService doctorUncheckedService;

    private static final Logger logger = LoggerFactory.getLogger(DoctorUncheckedController.class);




    @ApiOperation(value="医生注册",
            notes = "医生注册接口,注册成功errCode=0，否则错误信息返回至errMsg\n"+
                    "")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseBuilder<Object> register(@Validated @RequestBody DoctorUncheckedDTO doctorUnchecked, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage());
        }
        int re = -1;
        ResponseBuilder rb = new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS,"注册成功");
        try {
            re = doctorUncheckedService.register(doctorUnchecked);
            if(re != 1){
                rb = new ResponseBuilder(ErrCodeEnum.ERR_FAILED);
            }
        }catch (DataAccessException e){
            logger.error(e.getLocalizedMessage());
            rb = new ResponseBuilder(ErrCodeEnum.ERR_FAILED,"该电话号码已被注册");
        }
        return rb;
    }

    @ApiOperation(value="查询所有未审核医生",
            notes = "返回所有未审核医生")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseBuilder<Object> list(){
        ResponseBuilder rb = new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS,doctorUncheckedService.list());
        return rb;
    }


    @ApiOperation(value="删除未审核医生",
            notes = "通过电话号码删除未审核医生")
    @RequestMapping(value = "delete/{tel}", method = RequestMethod.DELETE)
    public ResponseBuilder<Object> delete(@PathVariable("tel")String phone){
        boolean flag = doctorUncheckedService.deleteByDoctorTel(phone);
        ResponseBuilder rb = null;
        if (flag){
             rb = new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS,"删除未审核医生成功！");
        }else {
            rb.setErrCode(ErrCodeEnum.ERR_FAILED.getErrCode());
            rb.setErrMsg("删除未审核医生失败，未通过手机号查找到该医生！");
            rb.setData(null);
        }
        return rb;
    }
}
