package com.xupt.outpatientms.enumeration;

public enum  ErrCodeEnum {
    ERR_SUCCESS(0,"操作成功"),
    ERR_FAILED(1,"操作失败"),
    ERR_NOTLOGIN(2,"未登录"),
    ERR_ARG(3,"参数错误")
    ;

    private int errCode;
    private String errMsg;

    ErrCodeEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
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
}
