package com.zsj.activiti7;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/17
 * 历史任务
 *
 */
@SpringBootTest
public class Qingjia2HistoricTaskInstance {

    @Autowired private HistoryService historyService;

    // 根据用户查询历史记录
    @Test
    public void historicTaskInstanceByUser()
    {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("bajie")
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        historicTaskInstances.forEach(this::info);
    }

    /**
     * 根据流程实例ID查询历史记录
     * 场景: 某个任务查询具体的流程经过了哪些步骤. 比如: 报销流程包括 发起报销、审核报销
     */
    @Test
    public void HistoricTaskInstanceByPID()
    {
        String processInstanceId = "a7c076c9-f8a2-11ea-9fc8-6045cb1b9054";
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().desc()
                .processInstanceId(processInstanceId)
                .list();
        historicTaskInstances.forEach(this::info);
    }

    private void info(HistoricTaskInstance historicTaskInstance) {
        System.out.println("===========================历史信息=================================");
        System.out.println("ID :" + historicTaskInstance.getId());
        System.out.println("流程实例ID :" + historicTaskInstance.getProcessInstanceId());
        System.out.println("Name :" + historicTaskInstance.getName());
        System.out.println("执行人 :" + historicTaskInstance.getAssignee());
        System.out.println("流程定义ID :" + historicTaskInstance.getProcessDefinitionId());
        System.out.println("删除原因 :" + historicTaskInstance.getDeleteReason());
        System.out.println("拾取时间 :" + historicTaskInstance.getClaimTime());
        System.out.println("描述 :" + historicTaskInstance.getDescription());
    }

}
