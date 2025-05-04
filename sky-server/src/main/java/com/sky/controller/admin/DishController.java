package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dish")
public class DishController {
    
    private final DishService dishService;
    
    
    /**
     * 新增菜品
     */
    @PostMapping()
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }
    
    /**
     * 菜品分页查询
     * GET请求 参数在URL中 不能使用@RequestBody
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 菜品删除
     * DELETE请求 参数在URL中 不能使用@RequestBody
     * 使用@RequestParam List<Long> ids     <=   1，2，3
     */
    @DeleteMapping()
    public Result delete(@RequestParam List<Long> ids) {
        dishService.deleteBatch(ids);
        return Result.success();
    }
}
