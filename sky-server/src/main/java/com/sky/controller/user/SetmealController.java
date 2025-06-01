package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 套餐管理
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@RequiredArgsConstructor
public class SetmealController {
    
    private final SetmealService setmealService;
    
    private final RedisTemplate redisTemplate;
    
    /**
     * 获取分类id下的 套餐列表
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(@RequestParam Long categoryId) {
        
        // 构造Redis当中的key 规则 dish_categoryId
        String key = "dish_" + categoryId;
        // 先从缓存中获取数据
        List<Setmeal> setmealList = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        if (setmealList != null && !setmealList.isEmpty()) {
            // 如果有，则直接返回,无需查询数据库
            return Result.success(setmealList);
        }
        
        // 如果没有，则从数据库查询,1然后存入缓存
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);
        
        setmealList = setmealService.list(setmeal);
        // 2将查询到的菜品列表存入缓存
        redisTemplate.opsForValue().set(key, setmealList, 120, TimeUnit.MINUTES);
        return Result.success(setmealList);
    }
    
    /**
     * 根据套餐id查询 包含的菜品列表
     *
     * @param id 套餐id
     * @return Result<Setmeal>
     */
    @GetMapping("/dish/list")
    public Result<SetmealVO> getSetmealWithDish(@RequestParam Long id) {
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }
    
}