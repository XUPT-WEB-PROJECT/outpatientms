package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.Doctor;
import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.common.Token;
import com.xupt.outpatientms.dto.DoctorLoginDTO;
import com.xupt.outpatientms.dto.UserLoginDTO;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.DoctorService;
import com.xupt.outpatientms.service.JwtService;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.vo.DoctorVO;
import com.xupt.outpatientms.vo.UserVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by BorisLiu on 2020/5/31
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private DoctorService doctorService;

    @ApiOperation(value="医生登录",
            notes = "医生登录接口，接口调用成功errCode=0，医生信息返回在data字段，token在Response Headers\"authorization\"字段中。否则错误信息返回至errMsg\n")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseBuilder<DoctorVO> login(@Validated @RequestBody DoctorLoginDTO doctor,
                                         BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"用户名或密码错误");
        }
        Doctor d = doctorService.login(doctor.getDoctorTel(),doctor.getDoctorPwd());
        if(d!=null){
            Token token = jwtService.refreshToken(d.getDoctorId()+"");
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            response.setHeader("Authorization","Bearer "+token.getToken());
            DoctorVO doctorVO = new DoctorVO();
            BeanUtils.copyProperties(d,doctorVO);
            return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "登录成功！",doctorVO);
        }else{
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "手机号或密码错误！");
        }
    }


    @ApiOperation(value="审核医生",
            notes = "审核医生")
    @RequestMapping(value = "review/{tel}", method = RequestMethod.POST)
    public ResponseBuilder<Object> review(@PathVariable("tel")String phone){
        boolean flag = doctorService.review(phone);
        ResponseBuilder<Object> rb = null;
        if (flag){
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS,"审核成功！");
        }else {
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"审核失败！");
        }
        return rb;
    }


}
