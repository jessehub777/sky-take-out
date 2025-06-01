package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 菜品管理
 */
@RestController("userDishController")
@RequiredArgsConstructor
@RequestMapping("/user/dish")
public class DishController {
    
    private final DishService dishService;
    
    private final RedisTemplate redisTemplate;
    
    
    /**
     * 根据id查询菜品
     */
    @GetMapping("/{id}")
    public Result<DishDTO> getById(@PathVariable Long id) {
        DishDTO dishDTO = dishService.getByIdWithFlavor(id);
        return Result.success(dishDTO);
    }
    
    /**
     * 获取分类下对应的菜品列表
     *
     * @return Result<String>
     */
    @GetMapping("/list")
    public Result<List<DishVO>> getDishListByCategoryId(@RequestParam Long categoryId) {
        
        // 构造Redis当中的key 规则 dish_categoryId
        String key = "dish_" + categoryId;
        
        // 先判断缓存中是否有数据
        List<DishVO> dishList = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (dishList != null && !dishList.isEmpty()) {
            // 如果有，则直接返回,无需查询数据库
            return Result.success(dishList);
        }
        
        
        // 如果没有，则从数据库查询,1然后存入缓存
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        
        dishList = dishService.listWidthFlavor(dish);
        // 2将查询到的菜品列表存入缓存
        redisTemplate.opsForValue().set(key, dishList, 120, TimeUnit.MINUTES);
        return Result.success(dishList);
    }
    
}
