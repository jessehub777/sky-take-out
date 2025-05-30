package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    
    /**
     * 根据分类id查询套餐的数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
    
    /**
     * 新增套餐
     *
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);
    
    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    
    /**
     * 批量删除套餐
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);
    
    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);
    
    /**
     * 查询套餐列表
     *
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);
    
    /**
     * 根据id修改套餐状态
     *
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void setStatus(Setmeal setmeal);
    
    /**
     * 修改套餐
     *
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);
}
