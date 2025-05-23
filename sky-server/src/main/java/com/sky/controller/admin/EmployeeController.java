package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@RequiredArgsConstructor()
public class EmployeeController {
    
    private final EmployeeService employeeService;
    private final JwtProperties jwtProperties;
    
    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        Employee employee = employeeService.login(employeeLoginDTO);
        
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        
        return Result.success(employeeLoginVO);
    }
    
    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
    
    
    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping()
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.save(employeeDTO);
        return Result.success();
    }
    
    /**
     * 员工分页查询。   参数不是json格式，不需要加@RequestBody
     *
     * @param employeePageQueryDTO
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 员工状态更新-启用禁用
     * 1.使用@PathVariable来接收status
     * 2.通过路径参数来接收id
     * Query方式是默认的请求方式,所以什么也不加
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, long id) {
        employeeService.startOrStop(status, id);
        return Result.success();
    }
    
    /**
     * 根据ID查询对应员工信息
     * 1.使用@PathVariable来接收 id
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable long id) {
        EmployeeDTO employee = employeeService.getUserById(id);
        return Result.success(employee);
    }
    
    /**
     * 根据ID更新对应员工信息
     * 1.使用@PathVariable来接收 id
     */
    @PutMapping("")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
