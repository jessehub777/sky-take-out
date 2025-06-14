package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@RequiredArgsConstructor
public class SetmealController {
    
    private final SetmealService setmealService;
    
    /**
     * 新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // 清除所有缓存
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }
    
    /**
     * 删除套餐
     * DELETE请求 参数在URL中 不能使用@RequestBody
     * 使用@RequestParam List<Long> ids     <=   1，2，3
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // 清除所有缓存
    public Result deleteBatch(@RequestParam List<Long> ids) {
        setmealService.deleteByIds(ids);
        return Result.success();
    }
    
    /**
     * 启用禁用套餐
     * DELETE请求 参数在URL中 不能使用@RequestBody
     */
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // 清除所有缓存
    public Result setStatus(@PathVariable("status") Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealService.setStatus(setmeal);
        return Result.success();
    }
    
    /**
     * 套餐分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable("id") Long id) {
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }
    
    @PutMapping()
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }
    
    
}