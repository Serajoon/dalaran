package com.serajoon.dalaran.support.cache.rediscache.example;

import com.serajoon.dalaran.common.util.MyDateTimeUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * redis例子service
 *
 * @author hanmeng
 * @since 2019/4/15 9:56
 */
@Service
@CacheConfig(cacheManager = "redisCacheManager", cacheNames = "hiibmp:user")
public class RedisExampleService {

    @Cacheable(key = "#id", unless = "#result == null")
    public User findById(String id) {
        System.err.println("数据库操作" + MyDateTimeUtils.getCurrentDateTimeStr());
        //数据库查询
        User user = new User();
        return user;
    }


    @CachePut(key = "#user.id", unless = "#result == null")
    public User updateUser(User user) {
        System.err.println("数据库操作" + MyDateTimeUtils.getCurrentDateTimeStr());
        user = new User();
        return user;
    }

    @CachePut(key = "#user.id", unless = "#result == null")
    public User saveUser(User user) {
        System.err.println("数据库操作" + MyDateTimeUtils.getCurrentDateTimeStr());
        user = new User();
        return user;
    }

    @CacheEvict(key = "#id")
    public int deleteUser(String id) {
        System.err.println("数据库操作" + MyDateTimeUtils.getCurrentDateTimeStr());
        User user = new User();
        return 1;
    }

}
