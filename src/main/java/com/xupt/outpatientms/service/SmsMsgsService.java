package com.xupt.outpatientms.service;

import com.xupt.outpatientms.component.SmsComponent;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.util.ResponseBuilder;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by BorisLiu on 2020/5/30
 */
@Service
public class SmsMsgsService {

    @Autowired
    public SmsComponent smsComponent;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseBuilder<Object> sendSmsCode(String phone) {
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        HttpResponse response = smsComponent.sendSmsCode(phone, code);
        if(response.getStatusLine().getStatusCode() == 200){
            stringRedisTemplate.opsForValue().set(phone, code, 20*60, TimeUnit.SECONDS);
            return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "发送成功");
        }
        int errCode = response.getStatusLine().getStatusCode();
        String errMsg = response.getStatusLine().getReasonPhrase();
        return new ResponseBuilder<>(errCode, errMsg);
    }

    public boolean check(String phone, String code){
        return Objects.equals(stringRedisTemplate.opsForValue().get(phone), code);
    }

}
