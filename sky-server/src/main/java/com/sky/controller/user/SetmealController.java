package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐管理
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@RequiredArgsConstructor
public class SetmealController {
    
    private final SetmealService setmealService;
    
    /**
     * 获取分类id下的 套餐列表
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")  // key: setmealCache::6
    public Result<List<Setmeal>> list(@RequestParam Long categoryId) {
        
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);
        List<Setmeal> setmealList = setmealService.list(setmeal);
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