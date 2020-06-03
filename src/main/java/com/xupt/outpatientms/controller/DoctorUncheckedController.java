package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.DoctorUnchecked;
import com.xupt.outpatientms.component.SmsComponent;
import com.xupt.outpatientms.dto.DoctorUncheckedDTO;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.DoctorUncheckedService;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.vo.DoctorUncheckedVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage());
        }
        int re = -1;
        ResponseBuilder<Object> rb = new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS,"注册成功");
        try {
            re = doctorUncheckedService.register(doctorUnchecked);
            if(re != 1){
                rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED);
            }
        }catch (DataAccessException e){
            logger.error(e.getLocalizedMessage());
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"该电话号码已被注册");
        }
        return rb;
    }

    @ApiOperation(value="查询所有未审核医生",
            notes = "返回所有未审核医生")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseBuilder<List<DoctorUncheckedVO>> list(){
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS,doctorUncheckedService.list());
    }


    @ApiOperation(value="删除未审核医生",
            notes = "通过电话号码删除未审核医生")
    @RequestMapping(value = "delete/{tel}", method = RequestMethod.DELETE)
    public ResponseBuilder<Object> delete(@PathVariable("tel")String phone){
        if(StringUtils.isEmpty(phone)
                || !phone.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")
        ) return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "电话号码不正确");
        boolean flag = doctorUncheckedService.deleteByDoctorTel(phone);
        ResponseBuilder<Object> rb = null;
        if (flag){
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS,"删除未审核医生成功！");
        }else {
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"删除未审核医生失败，未通过手机号查找到该医生！");
        }
        return rb;
    }
}
