package com.zsj.activiti7.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@Data
public class VariableInstanceVO implements Serializable {

    private String name;
    private String type;
    private String processInstanceId;
    private String taskId;
    private Boolean isTaskVariable;
    private Object value;

}
