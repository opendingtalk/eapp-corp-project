package com.util;

/**
 * 全局错误码
 * 如果业务方有自己的业务错误码,可以重新定义
 */
public enum ServiceResultCode {
    SYS_ERROR("-1","系统繁忙")
    ;

    private String errCode;
    private String errMsg;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    private ServiceResultCode(String errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
