package com.zsj.activiti7.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.activiti.api.process.model.ProcessInstance;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@Data
public class ProcessInstanceVO implements Serializable {

    private String id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    private String initiator;
    private String businessKey;
    private ProcessInstance.ProcessInstanceStatus status;
    private String processDefinitionId;
    private String processDefinitionKey;
    private Integer processDefinitionVersion;
    private String deploymentId;
    private String resourceName;
}
