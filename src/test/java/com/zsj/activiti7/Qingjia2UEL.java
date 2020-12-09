package com.zsj.activiti7;

import com.zsj.activiti7.entity.UEL_POJO;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/17
 * UEL表达式
 * 主要表: act_hi_varinst、act_ru_variable
 */
@SpringBootTest
public class Qingjia2UEL {

    @Autowired private RuntimeService runtimeService;

    @Autowired private TaskService taskService;

    // 启动流程实例带参数
    @Test
    public void startProcessInstanceWithArgs()
    {
        Map<String, Object> variables = new HashMap(); // 流程变量
        variables.put("assignee", "shaheshang");
        ProcessInstance processInstance = runtimeService.
                startProcessInstanceByKey("myProcess_UEL_V1", "sda", variables);
        System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
    }

    // 完成任务带参数,带流程变量
    @Test
    public void completeTaskWithArgs()
    {
        String taskId = "f0606f76-f8f1-11ea-9657-6045cb1b9054";
        Map<String, Object> map = new HashMap<>();
        map.put("pay", 105);
        taskService.complete(taskId, map);
        System.out.println("完成任务");
    }

    // 启动流程实例带参数,使用实体类, 数据库存json
    @Test
    public void startProcessInstanceWithClassArgs()
    {
        UEL_POJO pojo = new UEL_POJO();
        pojo.setAssignee("bajie");
        Map<String, Object> variables = new HashMap(); // 流程变量
        variables.put("uelpojo", pojo);
        ProcessInstance processInstance = runtimeService.
                startProcessInstanceByKey("myProcess_UEL_V3", "1236812", variables);
        System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
    }

    // 任务完成环节带参数,指定多个获选人
    @Test
    public void startProcessInstanceWithCandiDateArgs()
    {
        String taskId = "d7aac3cd-f8f5-11ea-b7c8-6045cb1b9054";
        Map<String,Object> map = new HashMap<>();
        map.put("candidate", "wukong,tangseng,shaheshang");
        taskService.complete(taskId, map); // 之后查询任务执行人依旧是null,因为需要先拾取
        System.out.println("完成任务");
    }

    // 直接指定流程变量
    @Test
    public void otherArgs()
    {
        String processInstanceId = "";
        runtimeService.setVariable(processInstanceId,"pay","101");
        /*runtimeService.setVariables(processInstanceId, null);

        taskService.setVariable(processInstanceId, "", "");
        taskService.setVariables(processInstanceId, null);*/
    }

    // 局部变量
    @Test
    public void localArgs()
    {
        String processInstanceId = "";
        runtimeService.setVariableLocal(processInstanceId,"pay","101");
     /*   runtimeService.setVariablesLocal(processInstanceId, null);

        taskService.setVariableLocal(processInstanceId, "", "");
        taskService.setVariablesLocal(processInstanceId, null);*/
    }

}
