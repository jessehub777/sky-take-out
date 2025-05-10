package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController("userDishController")
@RequiredArgsConstructor
@RequestMapping("/user/dish")
public class DishController {
    
    private final DishService dishService;
    
    
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
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        
        List<DishVO> list = dishService.listWidthFlavor(dish);
        return Result.success(list);
    }
    
}
