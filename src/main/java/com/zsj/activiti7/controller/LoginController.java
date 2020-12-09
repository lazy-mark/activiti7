package com.zsj.activiti7.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@RestController
public class LoginController {

    @GetMapping("/")
    public String index()
    {
        return "index";
    }

}
