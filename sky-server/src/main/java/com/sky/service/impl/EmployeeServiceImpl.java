package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeMapper employeeMapper;
    
    /**
     * 员工登录
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        
        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);
        
        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        
        //密码比对
        //对前端传过来的明文密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        
        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        
        //3、返回实体对象
        return employee;
    }
    
    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        
        //使用对象属性拷贝，来简化2个不同实体的转换
        BeanUtils.copyProperties(employeeDTO, employee);
        
        // 设置状态，使用常量类来表示enable
        employee.setStatus(StatusConstant.ENABLE);
        
        //设置密码，新增用户默认密码为123456，然后使用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        
        // 当前创建人的ID  通过JWT中的Token反向解析获取 然后存储到threadLocal.set(id)中
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        
        employeeMapper.insert(employee);
    }
    
    /**
     * 查询员工列表 分页查询，基于数据库层面
     *
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // select * from employee limit 0,10
        // 通过PageHelper.startPage()来实现分页查询--需要在pom.xml中添加pagehelper-spring-boot-starter的依赖
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        
        // 这里的PageHelper.startPage()方法会在查询之前调用，设置当前页和每页显示的条数
        // 这里的PageHelper会自动拦截查询语句，进行分页处理 通过ThreadLocal来实现,自动关联上下2条语句107,115
        
        
        // 这里传DTO的目的是参数中还有一个name属性
        // 左边的Page是pagehelper的Page类，里面封装了分页查询的结果
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        
        long total = page.getTotal();  // 通过page.getTotal()获取总记录数
        List<Employee> records = page.getResult(); // 通过page.getResult()获取当前页的记录
        records.forEach(record -> {
            record.setPassword("");
        });
        return new PageResult(total, records);
    }
    
    public void startOrStop(Integer status, long id) {
        // update employee set status = #{status} where id = #{id}
        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
        
        // 这里传输实体类
        employeeMapper.update(employee);
    }
    
    public EmployeeDTO getUserById(long id) {
        // select * from employee where id = #{id}
        // 如果是用实体类的话,需要employee.setPassword(“***”)
        return employeeMapper.getUserById(id);
    }
    
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }
    
    
}
