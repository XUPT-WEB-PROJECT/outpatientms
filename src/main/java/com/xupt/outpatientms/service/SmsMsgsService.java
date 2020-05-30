package com.xupt.outpatientms.service;

import com.xupt.outpatientms.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by BorisLiu on 2020/5/30
 */
@Service
public class SmsMsgsService {

    @Autowired
    public SmsComponent smsComponent;

    public String sendSmsCode(String phone){
       String code = (int)((Math.random()*9+1)*100000) + "";
       return smsComponent.sendSmsCode(phone,code);
    }

}
