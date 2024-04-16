package com.rootCauseMonitorSystem.exception;

/*
 * @Description:
 */
public class AllException extends Exception implements CommonError {

    private CommonError commonError;

    // 构造业务异常
    public AllException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    public AllException(CommonError commonError, String msg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(msg);
    }

    @Override
    public Integer getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getMsg() {
        return this.commonError.getMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
