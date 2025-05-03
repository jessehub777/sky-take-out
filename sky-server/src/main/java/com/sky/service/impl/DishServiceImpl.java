package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    
    private final DishMapper dishMapper;
    private final DishFlavorMapper dishFlavorMapper;
    
    /**
     * 新增菜品 其中同时新增菜品 和 新增口味，所以启用事务
     *
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        
        // 新增菜品 单个
        dishMapper.insert(dish);
        
        // 通过DishMapper.xml中 useGeneratedKeys="true" keyProperty="id"
        Long dishId = dish.getId();
        
        
        // 新增口味 多个 批量插入
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if (dishFlavors != null && !dishFlavors.isEmpty()) {
            for (DishFlavor dishFlavor : dishFlavors) {
                dishFlavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBatch(dishFlavors);
        }
    }
}
