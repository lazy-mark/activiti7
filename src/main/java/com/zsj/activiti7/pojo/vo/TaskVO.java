package com.zsj.activiti7.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.activiti.api.task.model.Task;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@Data
public class TaskVO implements Serializable {
    private String id;
    private String owner;
    private String assignee;
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date claimedDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dueDate;
    private int priority;
    private String processDefinitionId;
    private String processInstanceId;
    private Task.TaskStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date completedDate;
    private Long duration;
    private String processDefinitionVersion;
    private String businessKey;
    private boolean isStandalone;

    /* 扩展字段 */
    private String processInstanceName;

}
