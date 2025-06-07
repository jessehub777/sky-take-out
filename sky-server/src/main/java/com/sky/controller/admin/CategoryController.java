package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    private final RedisTemplate redisTemplate;
    
    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        // 清除所有缓存
        deleteCache();
        return Result.success();
    }
    
    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(Long id) {
        categoryService.deleteById(id);
        // 清除所有缓存
        deleteCache();
        return Result.success();
    }
    
    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        // 清除所有缓存
        deleteCache();
        return Result.success();
    }
    
    /**
     * 启用、禁用分类
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id) {
        categoryService.startOrStop(status, id);
        // 清除所有缓存
        deleteCache();
        return Result.success();
    }
    
    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<CategoryDTO>> list(Integer type) {
        List<CategoryDTO> list = categoryService.list(type);
        return Result.success(list);
    }
    
    /**
     * 清除所有缓存
     */
    private void deleteCache() {
        // 清除用户端分类缓存
        redisTemplate.delete("category_list_for_user");
    }
}
