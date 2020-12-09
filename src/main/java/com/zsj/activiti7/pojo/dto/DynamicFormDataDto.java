package com.zsj.activiti7.pojo.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/22
 */
@Data
public class DynamicFormDataDto implements Serializable {

    private String procDefId;
    private String procInstId;
    private String formKey;
    private JSONArray fields;

}
