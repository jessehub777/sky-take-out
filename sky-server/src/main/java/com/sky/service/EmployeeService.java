package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {
    
    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    
    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);
    
    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
    
    /**
     * 员工启用.禁用
     */
    void startOrStop(Integer status, long id);
    
    /**
     * 根据id查询单个员工信息
     *
     * @param id
     */
    EmployeeDTO getUserById(long id);
    
    void update(EmployeeDTO employeeDTO);
}
