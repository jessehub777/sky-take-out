package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@RequiredArgsConstructor
public class ShopController {
    
    //注入RedisTemplate
    private final RedisTemplate<String, String> redisTemplate;
    
    public static String KEY = "SHOP_STATUS";
    
    /**
     * 设置店铺营业状态
     *
     * @param status 店铺状态
     * @return Result<String>
     */
    @PutMapping("/{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set(KEY, String.valueOf(status));
        return Result.success();
    }
    
    /**
     * 获取店铺营业状态
     *
     * @return Result<String>
     */
    @GetMapping("status")
    public Result<Integer> getStatus() {
        Integer shopStatus = Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(KEY)));
        return Result.success(shopStatus);
    }
}
