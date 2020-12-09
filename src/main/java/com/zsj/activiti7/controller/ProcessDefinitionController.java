package com.zsj.activiti7.controller;

import com.zsj.activiti7.pojo.vo.DeploymentVO;
import com.zsj.activiti7.pojo.vo.ProcessDefinitionVO;
import com.zsj.activiti7.utils.ResultVO;
import com.zsj.activiti7.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@RestController
@RequestMapping("process/definition")
@Slf4j
@CrossOrigin
@Api(description = "流程定义管理")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public ResultVO uploadFile(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile multipartFile) {
        try {
            // 获取上传文件名
            String filename = multipartFile.getOriginalFilename();
            // 获取文件扩展名
            String extension = FilenameUtils.getExtension(filename);

            String target = "D:/bpmn/";

            filename = UUID.randomUUID() + filename;
            File file = new File(target + filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            multipartFile.transferTo(file);
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("filename", filename);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("上传文件失败")
                    .data("errorMsg", e.getMessage());
        }
    }


    // 添加流程定义: 通过上传BPMN
    @PostMapping("/uploadProcDef")
    @ApiOperation(value = "上传流程")
    public ResultVO uploadProcessDef(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile multipartFile
            , @RequestParam("deployName") String deployName) {
        try {
            // 获取上传文件名
            String filename = multipartFile.getOriginalFilename();
            // 获取文件扩展名
            String extension = FilenameUtils.getExtension(filename);
            // 获取字节流对象
            InputStream is = multipartFile.getInputStream();

            Deployment deployment = null;
            if (extension.equals("zip")) {
                ZipInputStream zipInputStream = new ZipInputStream(is);
                deployment = repositoryService.createDeployment()
                        .addZipInputStream(zipInputStream)
                        .name(deployName)
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()
                        .addInputStream(filename, is)
                        .name(deployName)
                        .deploy();
            }
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("rows", deployment.getId() + ";" + filename);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("部署流程失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    @GetMapping("/xml/{processInstanceId}")
    public ResultVO getBpmnXmlInfo(@PathVariable("processInstanceId") String processInstanceId)
    {
        try {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstanceId);
            BpmnXMLConverter converter = new BpmnXMLConverter();
            byte[] bytes = converter.convertToXML(bpmnModel);

            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("xml", new String(bytes));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("获取流程XML失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    // 添加流程定义: 通过在线提交BPMN的XML,
    @PostMapping("/online")
    public ResultVO online(@RequestParam("bpmn") String bpmn
            , @RequestParam("deployName") String deployName)
    {
        Deployment deployment = null;
        try {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            deployment = repositoryService.createDeployment()
                    .addString(uuid, bpmn)
                    .name(deployName)
                    .deploy();
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("deploymentId", deployment.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("部署流程失败")
                    .data("errorMsg", e.getMessage());
        }
    }

    // 获取流程定义列表
    @GetMapping("/list/{page}/{limit}")
    public ResultVO getProcessDefinitions(@PathVariable("page") Long page, @PathVariable("limit") Long limit)
    {
        try {
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .list();
            List<ProcessDefinitionVO> processDefinitionList = new ArrayList<>(processDefinitions.size());
            processDefinitions.forEach(processDefinition -> {
                ProcessDefinitionVO processDefinitionVO = new ProcessDefinitionVO();
                BeanUtils.copyProperties(processDefinition, processDefinitionVO);
                processDefinitionList.add(processDefinitionVO);
            });

            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("rows", processDefinitionList.stream()
                            .skip((page - 1) * limit)
                            .limit(limit)
                            .collect(Collectors.toList()))
                    .data("total", processDefinitions.size());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("获取流程定义失败")
                    .data("errorMsg",e.getMessage());
        }
    }

    // 获取流程定义XML
    @GetMapping("/xml")
    public void getProcessDef(@RequestParam("deploymentId") String deploymentId
            , @RequestParam("resourceName") String resourceName, HttpServletResponse response)
    {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] buffer = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(buffer) != -1) {
                outputStream.write(buffer);
            }
            inputStream.close();
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 获取流程部署列表
    @GetMapping("/deployments/{page}/{limit}")
    public ResultVO getProcessDeployments(@PathVariable("page") Long page, @PathVariable("limit") Long limit)
    {
        try {
            List<Deployment> deployments = repositoryService.createDeploymentQuery()
                    .list();
            List<DeploymentVO> deploymentVOList = new ArrayList<>(deployments.size());
            deployments.forEach(deployment -> {
                DeploymentVO deploymentVO = new DeploymentVO();
                BeanUtils.copyProperties(deployment, deploymentVO);
                deploymentVOList.add(deploymentVO);
            });
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data("rows", deploymentVOList.stream()
                            .skip((page - 1) * limit)
                            .limit(limit)
                            .sorted((t2,t1) -> t1.getDeploymentTime().compareTo(t2.getDeploymentTime()))
                            .collect(Collectors.toList()))
                    .data("total", deploymentVOList.size());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("获取流程部署列表失败")
                    .data("errorMsg", e.getMessage());
        }
    }


    // 删除流程定义和流程部署
    @DeleteMapping("/{deploymentId}")
    public ResultVO removeProcessDef(@PathVariable("deploymentId") String deploymentId)
    {
        try {
            repositoryService.deleteDeployment(deploymentId, true);
            return ResultVO.ok()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMsg())
                    .data(null);
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVO.error()
                    .code(ResponseCode.ERROR.getCode())
                    .message("删除流程定义失败")
                    .data("errorMsg", e.getMessage());
        }
    }


}
