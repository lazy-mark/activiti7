package com.zsj.activiti7.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@Data
public class DeploymentVO implements Serializable {

    private String id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deploymentTime;
    private String key;
    private Integer version;

}
