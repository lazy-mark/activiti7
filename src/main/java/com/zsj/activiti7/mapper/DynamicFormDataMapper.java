package com.zsj.activiti7.mapper;

import com.zsj.activiti7.pojo.entity.DynamicFormData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/22
 */
@Mapper
public interface DynamicFormDataMapper {

    int insertFormData(@Param("formData")DynamicFormData formData);

    DynamicFormData selectFormDataByFormKey(@Param("formKey") String formKey);

    DynamicFormData selectFormDataByProcInstId(@Param("processInstId") String processInstanceId);

    int deleteFormDataByFormKey(@Param("formKey") String formKey);
}
