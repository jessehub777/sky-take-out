package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    
    public User login(UserLoginDTO userLoginDTO) {
        String password = userLoginDTO.getPasswd();
        String mail = userLoginDTO.getMail();
        // 1. 根据邮箱查询用户
        User user = userMapper.getUserByMail(mail);
        // 2. 如果用户不存在，抛出异常
        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 3. 对前端传过来的明文密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 4. 比对密码
        if (!password.equals(user.getPasswd())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        // 5. 返回用户信息
        return user;
    }
    
    public void save(UserLoginDTO userLoginDTO) {
    
    }
    
    public UserLoginDTO getUserById(long id) {
        return null;
    }
    
    public void update(UserLoginDTO userLoginDTO) {
    
    }
}
