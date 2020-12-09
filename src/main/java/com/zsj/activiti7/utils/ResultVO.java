package com.zsj.activiti7.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@Data
@AllArgsConstructor
public class ResultVO {
    private Boolean success;
    private Integer code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    private ResultVO(){}

    public static ResultVO ok(){
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        resultVO.setCode(ResponseCode.SUCCESS.getCode());
        resultVO.setMessage("successful");
        return resultVO;
    }
    public static ResultVO error(){
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(false);
        resultVO.setCode(ResponseCode.ERROR.getCode());
        resultVO.setMessage("failure");
        return resultVO;
    }

    // 链式编程

    public ResultVO success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public ResultVO message(String message){
        this.setMessage(message);
        return this;
    }

    public ResultVO code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResultVO data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public ResultVO data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
