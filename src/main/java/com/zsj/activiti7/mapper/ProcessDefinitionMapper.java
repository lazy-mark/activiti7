package com.zsj.activiti7.mapper;

import com.zsj.activiti7.pojo.vo.ProcessDefinitionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/24
 */
@Mapper
public interface ProcessDefinitionMapper {

    List<ProcessDefinitionVO> selectProcessDefByProcDefIds(@Param("procDefIds") List<String> procDefIds);

}
