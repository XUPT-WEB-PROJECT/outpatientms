package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.component.SmsComponent;
import com.xupt.outpatientms.service.SmsMsgsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Created by BorisLiu on 2020/5/30
 */
@Controller
@RequestMapping("/smsmsgs")
public class SmsMsgsController {

    @Autowired
   public SmsMsgsService smsMsgsService;

    @ApiOperation(value="sendCode",
            notes = "测试发送手机验证码(请大家不要测试这个，短信次数有限)")
    @RequestMapping(value = "/sendCode/{phone}", method = RequestMethod.GET)
    @ResponseBody
    public String register(@PathVariable(value = "phone") String phone) {
        return smsMsgsService.sendSmsCode(phone);
    }
}
