package com.sky.controller.user;

import com.sky.dto.CategoryDTO;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分类管理
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    private final RedisTemplate redisTemplate;
    
    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<CategoryDTO>> list(Integer type) {
        
        // 构造Redis当中的key
        String key = "category_list_for_user";
        
        // 先判断缓存中是否有数据
        List<CategoryDTO> list = (List<CategoryDTO>) redisTemplate.opsForValue().get(key);
        if (list != null && !list.isEmpty()) {
            // 如果有，则直接返回,无需查询数据库
            return Result.success(list);
        }
        
        // 如果没有，则从数据库查询,1然后存入缓存
        list = categoryService.list(type);
        // 2将查询到的菜品列表存入缓存
        redisTemplate.opsForValue().set(key, list, 120, TimeUnit.MINUTES);
        return Result.success(list);
    }
}
