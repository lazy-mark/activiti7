package com.zsj.activiti7.controller;

import com.zsj.activiti7.SecurityUtil;
import com.zsj.activiti7.pojo.LoginUser;
import com.zsj.activiti7.pojo.vo.HistoricTaskVO;
import com.zsj.activiti7.utils.ResponseCode;
import com.zsj.activiti7.utils.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/20
 */
@RestController
@RequestMapping("history")
@Slf4j
@CrossOrigin
public class HistoryController {

    @Autowired private SecurityUtil securityUtil;

    @Autowired private HistoryService historyService;

    // 用户历史任务
    @GetMapping("/list/{page}/{limit}")
    public ResultVO getHistoryTaskByUsername(@AuthenticationPrincipal LoginUser loginUser
        , @PathVariable("page") Long page, @PathVariable("limit") Long limit)
    {
        try {
            // todo 测试代码
            if (loginUser == null) {
                loginUser = new LoginUser();
                loginUser.setUsername("bajie");
            }
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .taskAssignee(loginUser.getUsername())
                    .list();
            List<HistoricTaskVO> historicTaskVOList = new ArrayList<>();
            historicTaskInstances.forEach(historicTaskInstance -> {
                HistoricTaskVO historicTaskVO = new HistoricTaskVO();
                BeanUtils.copyProperties(historicTaskInstance, historicTaskVO);
                historicTaskVOList.add(historicTaskVO);
            });
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("rows", historicTaskVOList.stream()
                            .skip((page - 1) * limit)
                            .limit(limit)
                            .collect(Collectors.toList()))
                    .data("total", historicTaskVOList.size());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("获取历史用户任务失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    // 根据流程实例ID查询任务
    @GetMapping("/{processInstanceId}")
    public ResultVO getHistoryTaskByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId)
    {
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .processInstanceId(processInstanceId)
                    .list();
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("rows", historicTaskInstances);
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("获取历史用户任务失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    // todo 高亮显示流程历史
    @PostMapping("/highLight")
    public ResultVO highLightHistoricTask(@AuthenticationPrincipal LoginUser loginUser
            , @RequestParam("processInstanceId") String processInstanceId)
    {
        try {
            return ResultVO.ok();
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("高亮历史任务失败")
                    .data("errorMsg", e.getMessage());
        }
    }

}
