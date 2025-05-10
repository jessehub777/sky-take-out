package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    
    /**
     * 保存套餐 和菜品的关联关系
     *
     * @param setmealDTO
     * @return
     */
    void saveWithDish(SetmealDTO setmealDTO);
    
    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    
    /**
     * 批量删除套餐
     */
    void deleteByIds(List<Long> ids);
    
    /**
     * 启用禁用套餐
     *
     * @param setmeal
     */
    void setStatus(Setmeal setmeal);
    
    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    SetmealVO getById(Long id);
    
    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);
    
    /**
     * 查询套餐列表
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);
}
