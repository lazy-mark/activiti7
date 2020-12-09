package com.zsj.activiti7.security;

import com.zsj.activiti7.mapper.UserMapper;
import com.zsj.activiti7.pojo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Autowired private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*String password = passwordEncoder().encode("111");
        return new User(username, password, AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_ACTIVITI_USER"));*/

        LoginUser loginUser = userMapper.selectByUsername(username);
        if (null == loginUser) {
            throw new UsernameNotFoundException("用户不存在!");
        }
        log.info("{} 登录成功~~",loginUser);
        return loginUser;
    }

    @Bean
    private PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
