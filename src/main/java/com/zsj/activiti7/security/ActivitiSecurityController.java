package com.zsj.activiti7.security;

import com.zsj.activiti7.utils.ResultVO;
import org.springframework.web.bind.annotation.*;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@CrossOrigin
@RestController
public class ActivitiSecurityController {

    @PostMapping("login")
    public ResultVO login()
    {
        return ResultVO.ok().data("token","admin");
    }

    @GetMapping("info")
    public ResultVO info()
    {
        return ResultVO.ok().data("roles","[admin]").data("name","admin").data("avatar","./static/avatar/1.gif");
    }
}
