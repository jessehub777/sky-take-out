package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
public class UserLoginDTO implements Serializable {
    
    private String mail; // 邮箱
    
    private String passwd; // 密码
    
}
