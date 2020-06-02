package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.component.SmsComponent;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.SmsMsgsService;
import com.xupt.outpatientms.util.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * Created by BorisLiu on 2020/5/30
 */

@Controller
@RequestMapping("/smsmsgs")
@Api(tags = "短信验证接口")
public class SmsMsgsController {

    @Autowired
    public SmsMsgsService smsMsgsService;

    @ApiOperation(value = "sendCode",
            notes = "注册验证码(请大家不要测试这个，短信次数有限)")
    @RequestMapping(value = "/sendCode/{phone}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBuilder<Object> register(@PathVariable(value = "phone") String phone) {
        if(StringUtils.isEmpty(phone)
            || !phone.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "手机号码有误");
        }
        return smsMsgsService.sendSmsCode(phone);
    }

}
