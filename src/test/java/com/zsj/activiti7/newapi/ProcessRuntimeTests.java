package com.zsj.activiti7.newapi;

import com.zsj.activiti7.SecurityUtil;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.DeleteProcessPayloadBuilder;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@SpringBootTest
public class ProcessRuntimeTests {


    @Autowired private ProcessRuntime processRuntime;

    @Autowired private RepositoryService repositoryService;

    @Autowired private SecurityUtil securityUtil;

    /**
     * 获取流程实例
     */
    @Test
    public void getProcessInstance()
    {
        securityUtil.logInAs("bajie");
        Page<ProcessInstance> processInstancePage =
                processRuntime.processInstances(Pageable.of(0, 100));

        System.out.println("流程实例数量: " + processInstancePage.getTotalItems());

        List<ProcessInstance> processInstances = processInstancePage.getContent();

        processInstances.forEach(this::info);
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance()
    {
        securityUtil.logInAs("bajie");
        processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("myProcess_ProcessRuntime")
                .withName("第一个流程实例")
//                .withVariable("","")
                .withBusinessKey("业务编码")
                .build());
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance()
    {
        // 6a6aba21-f95f-11ea-ac57-6045cb1b9054
        // 6a6aba21-f95f-11ea-ac57-6045cb1b9054
        securityUtil.logInAs("bajie");
        processRuntime.delete(ProcessPayloadBuilder
                .delete()
                .withProcessInstanceId("6a6aba21-f95f-11ea-ac57-6045cb1b9054")
                .build());
    }


    /**
     * 挂起流程实例
     */
    @Test
    public void suspendProcessInstance()
    {
        securityUtil.logInAs("bajie");
        processRuntime.suspend(ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId("10c8666d-f8fb-11ea-b598-6045cb1b9054")
                .build());
    }


    /**
     * 激活流程实例
     */
    @Test
    public void activateProcessInstance()
    {
        securityUtil.logInAs("bajie");
        processRuntime.resume(ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId("10c8666d-f8fb-11ea-b598-6045cb1b9054")
                .build());
    }


    /**
     * 流程实例的参数
     */
    @Test
    public void getVariables()
    {
        securityUtil.logInAs("bajie");
        List<VariableInstance> variables = processRuntime.variables(ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId("cd3f0381-f8fb-11ea-9d2c-6045cb1b9054")
                .build());
        variables.forEach(variable -> {
            System.out.println("name: " + variable.getName());
            System.out.println("value: " + variable.getValue());
            System.out.println("taskId: " + variable.getTaskId());
            System.out.println("流程实例ID: " + variable.getProcessInstanceId());
            System.out.println("参数类型: " + variable.getType());
        });
    }


    private void info(ProcessInstance processInstance)
    {
        System.out.println("===============================================");
        System.out.println("流程实例ID: " + processInstance.getId());
        System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
        System.out.println("流程定义Key: " + processInstance.getProcessDefinitionKey());
        System.out.println("流程实例名称: " + processInstance.getName());
        System.out.println("开始时间: " + processInstance.getStartDate());
        System.out.println("流程实例状态: " + processInstance.getStatus());
    }
}
