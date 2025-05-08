package com.sky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        
        // 设置redis的连接工厂对象--在pom文件中配置了starter，会自动创建好，放到spring容器当中
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置redis key的序列化器--这样避免使用默认的jdk序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置redis value的序列化器
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
    
    
}
