package com.zsj.activiti7;

import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@SpringBootTest
public class TaskRuntimeTests {


    @Autowired private SecurityUtil securityUtil;
    @Autowired private TaskRuntime taskRuntime;

    /**
     * 获取当前登录用户任务
     */
    @Test
    public void getTasksByUser()
    {
        securityUtil.logInAs("wukong");
        Page<Task> page = taskRuntime.tasks(Pageable.of(0, 100));
        System.out.println("任务总数: " + page.getTotalItems());
        List<Task> tasks = page.getContent();
        tasks.forEach(this::info);
    }


    /**
     * 完成任务
     */
    @Test
    public void completeTask()
    {
        securityUtil.logInAs("wukong");
        Task task = taskRuntime.task("258a0a2e-f8f9-11ea-ad57-6045cb1b9054");
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim()
                    .withTaskId(task.getId())
                    .build());
        }
        taskRuntime.complete(TaskPayloadBuilder
                .complete()
                .withTaskId(task.getId())
                .build());
    }


    private void info(Task task)
    {
        System.out.println("执行人: " + task.getAssignee());
        System.out.println("流程实例Id: " + task.getProcessInstanceId());
        System.out.println("任务到期时间: " + task.getDueDate());
        System.out.println("任务name: " + task.getName());
        System.out.println("任务Id: " + task.getId());
        System.out.println("侯选人: " + task.getOwner());
        System.out.println("description: " + task.getDescription());
        System.out.println("================================================");
    }

}
