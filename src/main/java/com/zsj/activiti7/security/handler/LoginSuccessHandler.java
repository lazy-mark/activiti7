package com.zsj.activiti7.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsj.activiti7.utils.ResultVO;
import com.zsj.activiti7.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@Configuration
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.getWriter().write(
            objectMapper.writeValueAsString(
                    ResultVO.ok()
                            .code(ResponseCode.SUCCESS.getCode())
                            .message(ResponseCode.SUCCESS.getMsg())
                            .data("username", authentication.getName())
            )
        );
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

    }
}
