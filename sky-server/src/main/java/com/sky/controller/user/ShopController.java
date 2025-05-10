package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺管理")
@RequiredArgsConstructor
public class ShopController {
    
    //注入RedisTemplate
    private final RedisTemplate<String, String> redisTemplate;
    
    public static String KEY = "SHOP_STATUS";
    private final DishService dishService;
    
    /**
     * 获取店铺营业状态
     *
     * @return Result<String>
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer shopStatus = Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(KEY)));
        return Result.success(shopStatus);
    }
    

}
