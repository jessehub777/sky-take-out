package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺管理")
@RequiredArgsConstructor
public class ShopController {
    
    //注入RedisTemplate
    private final RedisTemplate<String, String> redisTemplate;
    
    /**
     * 设置店铺营业状态
     *
     * @param status 店铺状态
     * @return Result<String>
     */
    @PutMapping("/{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set("SHOP_STATUS", String.valueOf(status));
        return Result.success();
    }
    
    /**
     * 获取店铺营业状态
     *
     * @return Result<String>
     */
    @GetMapping("status")
    public Result<Integer> getStatus() {
        Integer shopStatus = Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get("SHOP_STATUS")));
        return Result.success(shopStatus);
    }
}
