package com.xupt.outpatientms.util;

import com.alibaba.fastjson.JSON;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "返回数据")
public class ResponseBuilder<T> {

    @ApiModelProperty(value = "错误码", required = true)
    private int errCode;

    @ApiModelProperty(value = "错误信息", required = true)
    private String errMsg;

    @ApiModelProperty(value = "数据域")
    private T data;

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

    public ResponseBuilder(ErrCodeEnum err, T data) {
        this.errCode = err.getErrCode();
        this.errMsg = err.getErrMsg();
        this.data = data;
    }

    public ResponseBuilder(ErrCodeEnum err, String errMsg, T data) {
        this.errCode = err.getErrCode();
        this.errMsg = errMsg;
        this.data = data;
    }

    public String build(){
        return JSON.toJSONString(this);
    }

}
