package com.zsj.activiti7.security;

import com.zsj.activiti7.security.handler.LoginFailureHandler;
import com.zsj.activiti7.security.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired private LoginSuccessHandler loginSuccessHandler;
    @Autowired private LoginFailureHandler loginFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
//                .loginPage("/admin/user/login")
                .loginProcessingUrl("/admin/user/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
    }

}
