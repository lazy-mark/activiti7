package com.zsj.activiti7;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/16
 * Deployment: 添加资源文件、获取部署信息、部署时间
 * 相关表: act_re_deployment
 */
@SpringBootTest
public class QingJiaDeployment {

    @Autowired private RepositoryService repositoryService;

    // 通过bpmn部署流程
    @Test
    public void initDeploymentBPMN() {
        String filename = "BPMN/ProcessRuntime.bpmn";
//        String pngname = "BPMN/qingjia10001.png";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(filename)
//                .addClasspathResource(pngname) // 部署图片,场景不多
                .name("测试新特性ProcessRuntime")
                .deploy();

        System.out.println(deployment.getName());
        System.out.println(deployment.getKey());
    }

    // 通过zip部署流程
    @Test
    public void initDeploymentZip() {
        InputStream is = this.getClass()
                .getClassLoader()
                .getResourceAsStream("BPMN/qingjia10001.zip");

        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(new ZipInputStream(is))
                .name("部署ZIP测试BPMN")
                .deploy();
        System.out.println(deployment.getName());
    }

    // 查询所有流程部署
    @Test
    public void getDeployments()
    {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        deployments.forEach(item -> {
            System.out.println("id:" + item.getId());
            System.out.println("name:" + item.getName());
            System.out.println("key:" + item.getKey());
            System.out.println("deployTime:" + item.getDeploymentTime());
            System.out.println("category:" + item.getCategory());
            System.out.println("====================================================");
        });
    }

    /**
     * 删除部署查看
     * @QingJiaProcessDefinition
     */

}
