package com.sky.mapper;

import com.sky.dto.DishDTO;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    
    /**
     * 批量插入菜品口味
     *
     * @param dishFlavors
     */
    void insertBatch(List<DishFlavor> dishFlavors);
    
    /**
     * 根据菜品id删除口味
     *
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);
    
    /**
     * 根据菜品id批量删除口味
     *
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);
    
    /**
     * 根据菜品id查询口味
     *
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
    
    /**
     * 根据菜品id更新口味
     *
     * @param dishDTO
     */
    void updateByDishId(DishDTO dishDTO);
}
