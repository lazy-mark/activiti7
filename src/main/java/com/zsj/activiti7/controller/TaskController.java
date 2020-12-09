package com.zsj.activiti7.controller;

import com.alibaba.fastjson.JSON;
import com.zsj.activiti7.SecurityUtil;
import com.zsj.activiti7.mapper.DynamicFormDataMapper;
import com.zsj.activiti7.pojo.dto.DynamicFormDataDto;
import com.zsj.activiti7.pojo.entity.DynamicFormData;
import com.zsj.activiti7.pojo.vo.TaskVO;
import com.zsj.activiti7.utils.ResponseCode;
import com.zsj.activiti7.utils.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@RestController
@RequestMapping("task")
@Slf4j
@CrossOrigin
public class TaskController {

    @Autowired private SecurityUtil securityUtil;

    @Resource private DynamicFormDataMapper formDataMapper;

    @Autowired private TaskRuntime taskRuntime;

    @Autowired private ProcessRuntime processRuntime;

    @Autowired private RepositoryService repositoryService;

    private static final boolean isDev = true;

    // 获取我的代办任务
    @GetMapping("/list/{page}/{limit}")
    public ResultVO getAssigneeTask(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit)
    {
        try {
            if (isDev) {
                securityUtil.logInAs("bajie");
            }
            Page<Task> taskPage = taskRuntime.tasks(Pageable.of((page - 1) * limit, limit));

            List<Task> tasks = taskPage.getContent();
            List<TaskVO> taskVOList = new ArrayList<>();
            tasks.forEach(task -> {
                TaskVO taskVO = new TaskVO();
                BeanUtils.copyProperties(task, taskVO);
                if (task.getAssignee() == null) {
                    taskVO.setAssignee("待拾取任务");
                }

                ProcessInstance processInstance = processRuntime.processInstance(task.getProcessInstanceId());
                taskVO.setProcessInstanceName(processInstance.getName());

                taskVOList.add(taskVO);
            });

            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("total", taskPage.getTotalItems())
                    .data("rows", taskVOList);
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("获取我的代办任务失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    // 完成任务
    @PostMapping("/{taskId}")
    public ResultVO completeTask(@PathVariable("taskId") String taskId)
    {
        try {
            if (isDev) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskId);
            if (null == task.getAssignee()) {
                taskRuntime.claim(TaskPayloadBuilder.claim()
                        .withTaskId(taskId)
                        .build());
            }
            taskRuntime.complete(TaskPayloadBuilder.complete()
                    .withTaskId(taskId)
                    .build());
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data(null);
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("完成" + taskId + "任务失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    /**
     * 渲染动态表单
     * 普通表单: 需要设置businessKey以及任务与表单一对一关系
     * 规定: 表单的key和任务的id一样
     * @param taskId
     * @return
     */
    @GetMapping("/{taskId}")
    @CrossOrigin
    public ResultVO formData(@PathVariable("taskId") String taskId) {
        try {
            if (isDev) {
                securityUtil.logInAs("bajie");
            }

            Task task = taskRuntime.task(taskId);

            // Activity_0dyw9rx
            UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                    .getFlowElement(task.getFormKey());

            if (userTask == null) {
                return ResultVO.ok()
                        .code(ResponseCode.SUCCESS.getCode())
                        .message(ResponseCode.SUCCESS.getMsg())
                        .data("rows", "无表单");
            }

            //某个实例保存的表单数据
            DynamicFormData dynamicFormData = formDataMapper.selectFormDataByProcInstId(task.getProcessInstanceId());

            String formKey = userTask.getFormKey();
            DynamicFormData formData = formDataMapper.selectFormDataByFormKey(formKey);
            DynamicFormDataDto formDataDto = new DynamicFormDataDto();
            BeanUtils.copyProperties(formData, formDataDto);
            formDataDto.setFields(JSON.parseArray(formData.getFields()));
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("rows", formDataDto);
        }catch (Exception e) {
            log.error(e.getMessage());
            return  ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("渲染动态表单失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    /**
     * 保存动态表单
     * 前端传递动态表单
     * @param taskId
     * @param dynamicFormDataDto
     * @return
     */
    @PostMapping("/{taskId}/dynamicForm")
    public ResultVO formDataSave(@PathVariable("taskId") String taskId, @RequestBody DynamicFormDataDto dynamicFormDataDto)
    {
        try {
            if (isDev) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskId);
            dynamicFormDataDto.setProcDefId(task.getProcessDefinitionId());
            dynamicFormDataDto.setProcInstId(task.getProcessInstanceId());
            dynamicFormDataDto.setFormKey(task.getFormKey());

            DynamicFormData formData = new DynamicFormData();
            BeanUtils.copyProperties(dynamicFormDataDto, formData);
            formData.setFields(dynamicFormDataDto.getFields().toString());

            log.info("generator dynamic form: {}", dynamicFormDataDto.getFields());
            formDataMapper.deleteFormDataByFormKey(task.getFormKey());
            formDataMapper.insertFormData(formData);

            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("rows", formData);
        }catch (Exception e) {
            log.error(e.getMessage());
            return  ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("保存动态表单失败")
                    .data("errorMsg", e.getMessage());
        }
    }

}
