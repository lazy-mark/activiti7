package com.zsj.activiti7;

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
 */
@SpringBootTest
public class Gateway {

    @Autowired private RuntimeService runtimeService;

    @Autowired private TaskService taskService;


    @Test
    public void startProcessInstance()
    {
        // myProcess_parallel
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_exclusive");
        System.out.println("流程定义ID:" + processInstance.getProcessDefinitionId());
    }

    @Test
    public void completeTask()
    {
        // eda8ec75-f8f8-11ea-bd7c-6045cb1b9054
        Map<String, Object> map = new HashMap<>();
        map.put("day", "100");
        taskService.complete("cd459335-f8fb-11ea-9d2c-6045cb1b9054",map);
        System.out.println("完成任务");
    }

}
