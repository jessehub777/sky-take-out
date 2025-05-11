package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
    
    
    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
    
    /**
     * 菜品批量删除
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);
    
    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    DishDTO getByIdWithFlavor(Long id);
    
    /**
     * 根据id修改菜品----修改菜品和口味
     *
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
    
    
    /**
     * 启售和停售菜品
     *
     * @param status
     * @param id
     */
    void status(Integer status, Integer id);
    
    /**
     * 根据分类id查询菜品列表
     *
     * @param categoryId
     * @return
     */
    List<Dish> getListByCategoryId(Long categoryId);
    

    List<DishVO> listWidthFlavor(Dish dish);
}
