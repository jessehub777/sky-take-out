package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    //邮箱
    private String mail;
    
    //姓名
    private String name;
    
    //密码
    private String passwd;
    
    //手机号
    private String phone;
    
    //注册时间
    private LocalDateTime createTime;
}
