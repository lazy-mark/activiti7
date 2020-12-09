package com.zsj.activiti7.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsj.activiti7.utils.ResultVO;
import com.zsj.activiti7.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {


    @Autowired private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        httpServletResponse.getWriter().write(
            objectMapper.writeValueAsString(
                    ResultVO.error()
                            .code(ResponseCode.ERROR.getCode())
                            .message(ResponseCode.ERROR.getMsg())
                            .data("msg", e.getMessage())
            )
        );
    }
}
