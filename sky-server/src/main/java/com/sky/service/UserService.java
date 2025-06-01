package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {
    
    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
    
    /**
     * 新增用户
     *
     * @param userLoginDTO
     */
    void save(UserLoginDTO userLoginDTO);
    
    /**
     * 根据id查询单个用户信息
     *
     * @param id
     */
    UserLoginDTO getUserById(long id);
    
    
    /**
     * 更新用户信息
     *
     * @param userLoginDTO
     */
    void update(UserLoginDTO userLoginDTO);
}
