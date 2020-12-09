package com.zsj.activiti7;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/17
 */
@SpringBootTest
public class Qingjia2Task {


    @Autowired private TaskService taskService;

    // 查询所有任务,后台管理员
    @Test
    public void getTasks()
    {
        List<Task> tasks = taskService.createTaskQuery().list();
        tasks.forEach(this::info);
    }

    // 查询我的代办任务,普通用户
    @Test
    public void getTaskByAssignee()
    {
        String assignee = "bajie";
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        tasks.forEach(this::info);
    }

    /**
     * 根据任务ID执行任务
     */
    @Test
    public void completeTaskById()
    {
        String taskId = "a7c6914d-f8a2-11ea-9fc8-6045cb1b9054";
        taskService.complete(taskId);
        System.out.println("完成任务");
    }

    /**
     * 拾取任务,和候选人有关,候选人支持多人,这些人就可以拾取任务
     * activiti6之前可以获取
     * activiti7整合了security,将无法获取
     */
    @Test
    public void claimTask()
    {
        // actviti7 error
        /*List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser("bajie").list();
        tasks.forEach(this::info);*/
        String taskId = "a2aea6ce-f8e7-11ea-a07a-6045cb1b9054";
        String userId = "bajie";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.claim(taskId, userId);
    }

    /**
     * 归还任务,任务拾取错了,需要将任务归还
     * 将执行人设置为空
     */
    @Test
    public void returnTask()
    {
        String taskId = "a2aea6ce-f8e7-11ea-a07a-6045cb1b9054";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.setAssignee(task.getId(), null);
    }

    /**
     * 交办任务
     * 人员A和人员B都是该任务的候选人,人员A先拾取了该任务,人员A觉得他完成不了这个任务,就将这个任务交给了人员B
     */
    @Test
    public void changeTask()
    {
        String taskId = "a2aea6ce-f8e7-11ea-a07a-6045cb1b9054";
        String userId = "wukong";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.setAssignee(task.getId(), userId);
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
        System.out.println("executionId: " + task.getExecutionId());
        System.out.println("================================================");
    }

}
