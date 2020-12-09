package com.zsj.activiti7.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/22
 */
@Data
public class DynamicFormData implements Serializable {

    private Long id;
    private String procDefId;
    private String procInstId;
    private String formKey;
    private String fields;

}
