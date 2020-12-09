package com.zsj.activiti7;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/17
 * ProcessDefinition和ProcessInstance关系: 1:n
 * 相关表: act_ru_execution、act_ru_task、act_ru_identitylink、act_ru_suspended_job
 */
@SpringBootTest
public class QingjiaProcessInstance {

    @Autowired private RuntimeService runtimeService;

    /**
     * 初始化流程实例
     * 影响表: act_ru_execution、act_ru_task、act_ru_identitylink
     */
    @Test
    public void initProcessInstance()
    {
        // 1、获取页面表单填写的内容、请假时间、请假事由
        // 2、将数据写入到我们自己创建的业务表中,业务表的主键 == businessKey
        // 3、通过businessKey将自己的业务和工作流数据关联起来了
        String key = "myProcess_UEL_V2";
        String businessKey = "32623";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, businessKey);
        System.out.println("流程实例ID: " + processInstance.getProcessInstanceId());
        System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
        System.out.println("流程定义名称:" + processInstance.getProcessDefinitionName());
    }

    /**
     * 获取流程实例列表
     * 从 act_re_procdef、act_ru_task 表中查
     */
    @Test
    public void getProcessInstances()
    {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        processInstances.forEach(processInstance -> info(processInstance));
    }

    /**
     * 挂起流程实例列表. 注意: 不能连续挂起两次
     * act_ru_task#SUSPENION_STATE修改为2
     */
    @Test
    public void suspendProcessInstance()
    {
        String instanceId = "a7c076c9-f8a2-11ea-9fc8-6045cb1b9054";
        runtimeService.suspendProcessInstanceById(instanceId);
    }

    /**
     * 激活流程实例. 注意: 不能连续激活两次
     * act_ru_task#SUSPENION_STATE修改为1
     */
    @Test
    public void activateProcessInstance()
    {
        String instanceId = "a7c076c9-f8a2-11ea-9fc8-6045cb1b9054";
        runtimeService.activateProcessInstanceById(instanceId);
    }

    // 删除流程实例. 注意: 不能删除两次
    @Test
    public void deleteProcessInstance()
    {
        String processInstanceId = "a904b19a-f83a-11ea-a623-6045cb1b9054";
        String reason = "测试删除";
        runtimeService.deleteProcessInstance(processInstanceId, reason);
        System.out.println(reason);
    }

    private void info(ProcessInstance processInstance)
    {
        System.out.println("===============================================");
        System.out.println("部署ID: " + processInstance.getDeploymentId());
        System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
        System.out.println("流程定义名称: " + processInstance.getProcessDefinitionName());
        System.out.println("流程定义Key: " + processInstance.getProcessDefinitionKey());
        System.out.println("流程实例ID: " + processInstance.getProcessInstanceId());
        System.out.println("开始时间: " + processInstance.getStartTime());
        System.out.println("userId: " + processInstance.getStartUserId());
        System.out.println("isEnabled: " + processInstance.isEnded());
        System.out.println("isSuspend: " + processInstance.isSuspended());
    }

}
