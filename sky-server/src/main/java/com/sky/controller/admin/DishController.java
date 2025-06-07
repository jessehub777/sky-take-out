package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController("adminDishController")
@RequiredArgsConstructor
@RequestMapping("/admin/dish")
public class DishController {
    
    private final DishService dishService;
    private final RedisTemplate redisTemplate;
    
    
    /**
     * 新增菜品
     */
    @PostMapping()
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        
        // 清除对应分类ID的缓存(单个)
        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);
        
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
        clearAllCache();
        
        return Result.success();
    }
    
    /**
     * 根据id查询菜品
     */
    @GetMapping("/{id}")
    public Result<DishDTO> getById(@PathVariable Long id) {
        DishDTO dishDTO = dishService.getByIdWithFlavor(id);
        return Result.success(dishDTO);
    }
    
    /**
     * 修改菜品
     */
    @PutMapping()
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.updateWithFlavor(dishDTO);
        clearAllCache();
        
        return Result.success();
    }
    
    /**
     * 根据分类id获取对应的菜品列表
     */
    @GetMapping("/list")
    public Result<List<Dish>> getListByCategoryId(Long categoryId) {
        List<Dish> list = dishService.getListByCategoryId(categoryId);
        return Result.success(list);
    }
    
    /**
     * 菜品停售/启售
     */
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, @RequestParam Integer id) {
        dishService.status(status, id);
        clearAllCache();
        
        return Result.success();
    }
    
    /**
     * 抽取一个公共方法用于清除缓存
     */
    private void clearAllCache() {
        // 清除缓存--不一定对应哪个categoryId,所以直接删掉全部的就行了.
        // 删除所有以 "dish_" 开头的缓存
        Set keys = redisTemplate.keys("dish_*");
        // delete方法可以接受一个Set集合,将所有匹配的key都删除
        redisTemplate.delete(keys);
    }
}
