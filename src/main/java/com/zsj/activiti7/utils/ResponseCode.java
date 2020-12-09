package com.zsj.activiti7.utils;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
public enum ResponseCode {

    SUCCESS(0, "成功"),
    ERROR(1, "失败"),
    ;

    private Integer code;
    private String msg;
    ResponseCode(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
