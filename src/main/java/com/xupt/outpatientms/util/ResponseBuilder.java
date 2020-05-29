package com.xupt.outpatientms.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import lombok.Data;

public class ResponseBuilder {

    private int errCode;
    private String errMsg;
    private Object data;

    public ResponseBuilder(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public ResponseBuilder(ErrCodeEnum err) {
        this.errCode = err.getErrCode();
        this.errMsg = err.getErrMsg();
    }

    public ResponseBuilder(ErrCodeEnum err, String errMsg) {
        this.errCode = err.getErrCode();
        this.errMsg = errMsg;
    }

    public ResponseBuilder(ErrCodeEnum err, Object data) {
        this.errCode = err.getErrCode();
        this.errMsg = err.getErrMsg();
        this.data = data;
    }

    public String build(){
        return JSON.toJSONString(this);
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
