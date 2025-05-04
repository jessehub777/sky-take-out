package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
    private final SetmealDishMapper setmealDishMapper;
    
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
    
    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        
        return new PageResult(page.getTotal(), page.getResult());
    }
    
    /**
     * 菜品批量删除 启用事务
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断： 启售中的菜品无法删除
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                // 启售中的菜品无法删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断：有套餐的菜品无法删除
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        
        // 循环删除菜品 -----性能不高，可以使用SQL的 in 批量删除
//        for (Long id : ids) {
//            // 删除单个菜品
//            dishMapper.deleteById(id);
//            // 删除菜品后，菜品口味也要删除
//            dishFlavorMapper.deleteByDishId(id);
//        }
        
        // 批量删除菜品--改进版
        dishMapper.deleteByIds(ids);
        // 批量删除菜品口味--改进版
        dishFlavorMapper.deleteByDishIds(ids);
    }
}
