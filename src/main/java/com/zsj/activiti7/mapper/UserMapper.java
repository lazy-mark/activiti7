package com.zsj.activiti7.mapper;

import com.zsj.activiti7.pojo.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @desc:
 * @author: zhangshengjun
 * @createDate: 2020/9/18
 */
@Mapper
@Component
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    LoginUser selectByUsername(@Param("username") String username);


}
