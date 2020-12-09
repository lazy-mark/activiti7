package com.zsj.activiti7.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@Data
public class HistoricTaskVO extends TaskVO implements Serializable {

    private String id;

    private String deleteReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
    private Long durationInMillis;
    private Long workTimeInMillis;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date claimTime; /* 拾取时间 */

    private String executionId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    private String taskDefinitionKey;
    private String category;
    private String formKey; // business key ?
    private Map<String, Object> taskLocalVariables;
    private Map<String, Object> processVariables;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date time;

}
