package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        HashOperations hashOperations = redisTemplate.opsForHash();
        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }
    
    /**
     * 操作String类型的数据
     */
    @Test
    public void testString() {
        // set get setex setnx
        
        redisTemplate.opsForValue().set("city", "kumagaya");
        String city = redisTemplate.opsForValue().get("city");
        System.out.println(city);
        
        // 设置过期时间setex
        redisTemplate.opsForValue().set("code", "1234", 10, TimeUnit.SECONDS);
        
        // 设置过不重复添加setnx
        redisTemplate.opsForValue().setIfAbsent("code11", "1234");
        // 如果key存在则不设置setnx  所以实际下面的代码不会插入数据
        redisTemplate.opsForValue().setIfAbsent("code11", "333");
    }
    
    /**
     * 操作Hash类型的数据
     */
    @Test
    public void testHash() {
        // hset hget hdel hkeys hvals
        HashOperations hashOperations = redisTemplate.opsForHash();
        
        // hset
        hashOperations.put("user", "name", "Jesse");
        hashOperations.put("user", "age", "22");
        
        // hget
        Object o = hashOperations.get("user", "name");
        System.out.println(o);
        
        
        // hkeys
        Set user = hashOperations.keys("user");
        System.out.println(user);
        
        // hvals
        List user1 = hashOperations.values("user");
        System.out.println(user1);
        
        // hdel
        hashOperations.delete("user", "name");
    }
    
    /**
     * 操作List类型的数据
     */
    @Test
    public void testList() {
        // lpush lrange rpop llen
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        
        // lpush 从左边插入，倒序，后面插入的在前面
        listOperations.leftPush("list", "1");
        listOperations.leftPush("list", "2");
        listOperations.leftPush("list", "3");
        
        // lrange 遍历list
        List<String> list = listOperations.range("list", 0, -1);
        System.out.println(list);
        
        // rpop 从右边弹出 删除最后一个 返回值是被删除的值
        String popped = listOperations.rightPop("list");
        System.out.println("被弹出的元素：" + popped);
        
        
        // llen
        Long size = listOperations.size("list");
        System.out.println("当前 list 长度：" + size);
    }
    
    /**
     * 操作Set类型的数据
     */
    @Test
    public void testSet() {
        // sadd smembers scard sinner sunion srem
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        // sadd
        setOperations.add("set1", "1");
        setOperations.add("set1", "2");
        setOperations.add("set1", "3");
        setOperations.add("set1", "4");
        setOperations.add("set1", "5");
        setOperations.add("set1", "6");
        
        setOperations.add("set2", "1", "2", "a", "b", "c", "d", "e");
        
        // smembers
        Set<String> set = setOperations.members("set1");
        System.out.println(set);
        Set<String> set2 = setOperations.members("set2");
        System.out.println(set2);
        
        
        // scard
        Long size = setOperations.size("set1");
        System.out.println("set1长度：" + size);
        
        // sinner
        Set<String> intersect = setOperations.intersect("set1", "set2");
        System.out.println("set1 和 set2 的交集：" + intersect);
        
        // sunion
        Set<String> union = setOperations.union("set1", "set2");
        System.out.println("set1 和 set2 的并集：" + union);
        
        // srem
        Long remove = setOperations.remove("set1", "1");
        System.out.println("删除的元素个数：" + remove);
        Set<String> setAfterRemove = setOperations.members("set1");
        System.out.println("删除 1 后的 set1：" + setAfterRemove);
        
    }
    
    /**
     * 操作ZSet类型的数据
     */
    @Test
    public void testZSet() {
        // zadd zrange zincrby zrem
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        
        // zadd
        zSetOperations.add("zset1", "a", 1);
        zSetOperations.add("zset1", "b", 2);
        zSetOperations.add("zset1", "c", 3);
        zSetOperations.add("zset1", "d", 4);
        zSetOperations.add("zset1", "a", 5); // 如果元素已经存在，则更新分数
        
        // zrange
        Set<String> range = zSetOperations.range("zset1", 0, -1);
        System.out.println("range=> " + range);
        
        // zincrby
        Double increment = zSetOperations.incrementScore("zset1", "a", 2);
        System.out.println("a的分数增加2后：" + increment);
        Set<String> rangeAfterIncrement = zSetOperations.range("zset1", 0, -1);
        System.out.println(rangeAfterIncrement);
        
        // zrem
        Long remove = zSetOperations.remove("zset1", "a");
        System.out.println("删除的元素个数：" + remove);
        Set<String> rangeAfterRemove = zSetOperations.range("zset1", 0, -1);
        System.out.println("range=> " + rangeAfterRemove);
    }
    
    /**
     * Redis通用操作 不区分数据类型
     */
    @Test
    public void testCommon() {
        // opsForValue opsForHash opsForList opsForSet opsForZSet
        // 直接使用redisTemplate的opsForXxx方法获取对应的操作对象
        // 也可以使用redisTemplate.execute方法执行原生的redis命令
        // redisTemplate.execute();
        
        redisTemplate.delete("keyName");
        redisTemplate.hasKey("keyName");
        redisTemplate.expire("keyName", 10, TimeUnit.SECONDS);
        redisTemplate.getExpire("keyName", TimeUnit.SECONDS);
        
    }
}
