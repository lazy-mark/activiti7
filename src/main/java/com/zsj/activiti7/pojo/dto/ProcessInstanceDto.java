package com.zsj.activiti7.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@Data
public class ProcessInstanceDto implements Serializable {

    private String processDefinitionKey;
    private String processInstanceName;
    private String instanceVariable;
    private String businessKey;

}
