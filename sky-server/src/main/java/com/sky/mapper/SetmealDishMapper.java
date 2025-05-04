package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    
    /**
     * 根据 多个菜品id 查询 多个套餐id
     *
     * @param dishIds
     * @return
     */
    // select setmeal_id from setmeal_dish where dish_id in (1, 2, 3)
    public List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
