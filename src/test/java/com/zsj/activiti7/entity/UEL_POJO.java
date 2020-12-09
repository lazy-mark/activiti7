package com.zsj.activiti7.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/17
 * 1. 类必须实例化
 * 2. 字段必须小写
 */
@Data
public class UEL_POJO implements Serializable {
    private String assignee;
    private String pay;
}
