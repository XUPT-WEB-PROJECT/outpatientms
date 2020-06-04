package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.Doctor;
import com.xupt.outpatientms.bean.DoctorUnchecked;
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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
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
        Doctor d = doctorService.login(doctor.getDoctorTel(),DigestUtils.md5Hex(doctor.getDoctorPwd()));
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


    @ApiOperation(value="审核医生注册信息", notes = "将医生注册信息转换为正式医生信息，审核成功errCode = 0，并将删去的审核信息保存至data字段，否则错误信息保存在errMsg")
    @RequestMapping(value = "review/{tel}", method = RequestMethod.POST)
    public ResponseBuilder<DoctorUnchecked> review(@PathVariable("tel")String phone){
        if(StringUtils.isEmpty(phone)
               || !phone.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")
        ) return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "电话号码不正确");
        DoctorUnchecked unchecked = doctorService.checkDoctorUncheckedTel(phone);
        if(unchecked == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"电话号码不正确");
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(unchecked,doctor);
        ResponseBuilder<DoctorUnchecked> rb = null;
        boolean flag = false;
        try{
             flag = doctorService.review(doctor);
        }catch (Exception e){
             rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"审核失败！");
             return rb;
        }
        if (flag){
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS,"审核成功！", unchecked);
        }else {
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"审核失败！");
        }
        return rb;
    }


}
