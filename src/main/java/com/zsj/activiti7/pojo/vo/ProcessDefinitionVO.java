package com.zsj.activiti7.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@Data
public class ProcessDefinitionVO implements Serializable {

    private String id;
    private String key;
    private String name;
    private String resourceName;
    private Integer version;
    private String deploymentId;
    private String engineVersion;

}
