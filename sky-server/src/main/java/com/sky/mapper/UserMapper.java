package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    
    /**
     * 根据邮箱查询用户信息
     *
     * @param mail
     * @return
     */
    @Select("select * from user where mail = #{mail}")
    User getUserByMail(String mail);
    
}
