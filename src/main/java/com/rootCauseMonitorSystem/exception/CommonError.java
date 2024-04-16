package com.rootCauseMonitorSystem.exception;

/*
 * @Description: 报错接口
 */
public interface CommonError {
    Integer getErrCode();

    String getMsg();

    CommonError setErrMsg(String errMsg);
}

