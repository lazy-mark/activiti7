package com.zsj.activiti7;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/17
 * ProcessDefinition: 获取版本号、key、资源名称、部署ID等
 * 相关表: act_re_procdef
 */
@SpringBootTest
public class QingJiaProcessDefinition {


    @Autowired private RepositoryService repositoryService;

    // 查询流程定义
    @Test
    public void getDefinitions()
    {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        processDefinitions.forEach(item -> {
            System.out.println("========================流程定义信息================================");
            System.out.println("id: "+item.getId()); // 格式: key:version:deploymentId
            System.out.println("name: "+item.getName());
            System.out.println("key: "+item.getKey());
            System.out.println("category: "+item.getCategory());
            System.out.println("deploymentId: "+item.getDeploymentId());
            System.out.println("resourceName: "+item.getResourceName());
            System.out.println("version: "+item.getVersion());
        });

    }

    // 根据部署Id删除流程部署和流程定义
    @Test
    public void delete()
    {
        String did = "ff6f8f95-f898-11ea-b538-6045cb1b9054";
        repositoryService.deleteDeployment(did);
        System.out.println("删除成功");
    }

}
