package com.zsj.activiti7.controller;

import com.zsj.activiti7.SecurityUtil;
import com.zsj.activiti7.mapper.ProcessDefinitionMapper;
import com.zsj.activiti7.pojo.LoginUser;
import com.zsj.activiti7.pojo.dto.ProcessInstanceDto;
import com.zsj.activiti7.pojo.vo.ProcessDefinitionVO;
import com.zsj.activiti7.pojo.vo.ProcessInstanceVO;
import com.zsj.activiti7.pojo.vo.VariableInstanceVO;
import com.zsj.activiti7.utils.ResultVO;
import com.zsj.activiti7.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@RestController
@RequestMapping("process/instance")
@Slf4j
@CrossOrigin
public class ProcessInstanceController {

    @Autowired private RepositoryService repositoryService;

    @Autowired private SecurityUtil securityUtil;

    @Autowired private ProcessRuntime processRuntime;

    @Resource private ProcessDefinitionMapper processDefinitionMapper;

    private static final Boolean isDev = true;

    // 查询流程实例
    @GetMapping("/{page}/{limit}")
    public ResultVO getProcessInstances(@AuthenticationPrincipal LoginUser loginUser
            , @PathVariable("page") Integer page, @PathVariable("limit") Integer limit)
    {
        try {
            if (isDev) {
                securityUtil.logInAs("bajie");
            }
            Page<ProcessInstance> processInstancePage = processRuntime.processInstances(
                    Pageable.of(((page - 1) * limit), limit)
            );
            List<ProcessInstance> processInstances = processInstancePage.getContent();

            List<String> ids = processInstances.stream()
                    .map(ProcessInstance::getProcessDefinitionId)
                    .collect(Collectors.toList());

            // 根据流程定义Id列表批量查,可以通过Mybatis查询
            List<ProcessDefinitionVO> processDefinitions = processDefinitionMapper.selectProcessDefByProcDefIds(ids);

            Map<String, ProcessDefinitionVO> processDefinitionMap = new HashMap<>();
            processDefinitions.forEach(processDefinition -> processDefinitionMap.put(processDefinition.getId(), processDefinition));

            List<ProcessInstanceVO> processInstanceVOList = new ArrayList<>(processInstances.size());
            processInstances.forEach(processInstance -> {
                ProcessInstanceVO processInstanceVO = new ProcessInstanceVO();
                BeanUtils.copyProperties(processInstance, processInstanceVO);

                if (processDefinitionMap.containsKey(processInstance.getProcessDefinitionId())) {
                    ProcessDefinitionVO processDefinition = processDefinitionMap.get(processInstance.getProcessDefinitionId());
                    processInstanceVO.setResourceName(processDefinition.getResourceName());
                    processInstanceVO.setDeploymentId(processDefinition.getDeploymentId());
                }

                processInstanceVOList.add(processInstanceVO);
            });
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("total", processInstancePage.getTotalItems())
                    .data("rows",  processInstanceVOList.stream()
                            .sorted((t2, t1) -> t1.getStartDate().compareTo(t2.getStartDate()))
                            .collect(Collectors.toList()));
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.ok()
                    .code(ResponseCode.ERROR.getCode())
                    .message("查询流程实例失败")
                    .data(null);
        }
    }

    //启动流程实例
    @PostMapping("/start")
    public ResultVO startProcessInstance(@RequestBody ProcessInstanceDto processInstanceDto, @AuthenticationPrincipal LoginUser loginUser)
    {
        try {
            if (isDev) {
                loginUser = new LoginUser();
                loginUser.setUsername("bajie");
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(processInstanceDto.getProcessDefinitionKey())
                    .withName(processInstanceDto.getProcessInstanceName())
                    .withBusinessKey(processInstanceDto.getBusinessKey())
                    .build());
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("id", processInstance.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("启动流程实例失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    // 挂起流程实例
    @PostMapping("/suspend/{processInstanceId}")
    public ResultVO suspendProcessInstance(@AuthenticationPrincipal LoginUser loginUser
            , @PathVariable("processInstanceId") String processInstanceId)
    {
        try {
            if (isDev) {
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId(processInstanceId)
                    .build());
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("name", processInstance.getName())
                    .data("status", processInstance.getStatus().name());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("挂起流程实例失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    // 激活流程实例
    @PostMapping("/activate/{processInstanceId}")
    public ResultVO activateProcessInstance(@PathVariable("processInstanceId") String processInstanceId
            , @AuthenticationPrincipal LoginUser loginUser)
    {
        try {
            if (isDev) {
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId(processInstanceId)
                    .build());
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("name", processInstance.getName())
                    .data("status", processInstance.getStatus().name());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("激活流程实例失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    @DeleteMapping("/{processInstanceId}")
    public ResultVO deleteProcessInstance(@PathVariable("processInstanceId") String processInstanceId
            , @AuthenticationPrincipal LoginUser loginUser)
    {
        try {
            if (isDev) {
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(processInstanceId)
                    .build());
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("name", processInstance.getName());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("删除流程实例失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    // 查询流程参数
    @GetMapping("/variables/{processInstanceId}")
    public ResultVO getProcessVariables(@AuthenticationPrincipal LoginUser loginUser
            , @PathVariable("processInstanceId") String processInstanceId)
    {
        if (isDev) {
            securityUtil.logInAs("bajie");
        }
        try {
            List<VariableInstance> variables = processRuntime.variables(ProcessPayloadBuilder
                    .variables()
                    .withProcessInstanceId(processInstanceId)
                    .build());
            List<VariableInstanceVO> variableInstanceVOList = new ArrayList<>(variables.size());
            variables.forEach(variableInstance -> {
                VariableInstanceVO variableInstanceVO = new VariableInstanceVO();
                BeanUtils.copyProperties(variableInstance, variableInstanceVO);
                variableInstanceVOList.add(variableInstanceVO);
            });
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("rows", variableInstanceVOList);
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("获取流程参数失败")
                    .data("errorMsg", e.getMessage());
        }
    }

}
