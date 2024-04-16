package com.rootCauseMonitorSystem.exception;

/*
 * @Description:
 */
public enum EmAllException implements CommonError{
    //邮箱相关
    EMAIL_SEND_WRONG(403, "邮箱验证码获取失败"),
    ;

    // 错误码
    private Integer code;

    // 错误信息
    private String msg;

    EmAllException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getErrCode() {
        return this.code;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.msg = errMsg;
        return this;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
