package com.atguigu.admin.mapper;

import com.atguigu.admin.bean.LoginUserInfo;
import org.springframework.data.repository.query.Param;

public interface queryMapper {
    public LoginUserInfo queryLoginInfo(@Param("loginName") String loginName , @Param("password")String password);
    public Integer queryLoginInfoByLoginName(@Param("loginName") String loginName);
}
