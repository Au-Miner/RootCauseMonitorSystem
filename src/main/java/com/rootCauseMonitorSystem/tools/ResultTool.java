package com.rootCauseMonitorSystem.tools;


import com.rootCauseMonitorSystem.exception.EmAllException;
import com.rootCauseMonitorSystem.model.response.Result;

import java.util.List;

/*
 * @Description:
 */
public class ResultTool {

    @SuppressWarnings("unchecked")
    public static Result success(List<Object> objectList){
        Result result = new Result();
        result.setCode(200);
        result.setData(objectList);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Result success(Object object){
        Result result = new Result();
        result.setCode(200);
        result.setData(object);
        return result;
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(200);
        return result;
    }

    public static Result error(EmAllException e) {
        Result result = new Result();
        result.setCode(e.getErrCode());
        result.setMessage(e.getMsg());
        return result;
    }

    public static Result error(Integer code, String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    public static Result error(Integer code){
        Result result = new Result();
        result.setCode(code);
        return result;
    }
}
