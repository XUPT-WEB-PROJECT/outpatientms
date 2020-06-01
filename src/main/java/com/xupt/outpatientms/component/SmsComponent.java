package com.xupt.outpatientms.component;


import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.util.HttpUtils;
import com.xupt.outpatientms.util.ResponseBuilder;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BorisLiu on 2020/5/30
 */
@Component
@Data
@ConfigurationProperties(prefix = "smsmsgs.msgcode")
public class SmsComponent {

    private static final Logger logger = LoggerFactory.getLogger(SmsComponent.class);

    private String host;
    private String path;
    private String method;
    private String appcode;
    private String sign;
    private String skin;

    public HttpResponse sendSmsCode(String phone, String code){
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("param", code);
        querys.put("phone", phone);
        querys.put("sign", sign);
        querys.put("skin", skin);
        try {
            logger.info("[验证码相关参数]:"+"code:"+code + ",phone:" + phone + ",sign:" + sign + ",skin:" + skin );
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
           return HttpUtils.doGet(host, path, method, headers, querys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
