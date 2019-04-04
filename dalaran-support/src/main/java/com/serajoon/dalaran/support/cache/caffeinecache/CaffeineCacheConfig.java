package com.serajoon.dalaran.support.cache.caffeinecache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@AutoConfigureAfter(CaffeineCacheProperties.class)
@EnableConfigurationProperties(CaffeineCacheProperties.class)
@Slf4j
public class CaffeineCacheConfig {

    @Autowired
    private CaffeineCacheProperties caffeineCacheProperties;

    @Bean
    public CacheManager cacheManagerWithCaffeine() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                .recordStats()
                .initialCapacity(caffeineCacheProperties.getInitialcapacity())
                .maximumSize(caffeineCacheProperties.getMaximumsize())
                .expireAfterAccess(30, TimeUnit.HOURS)
                .removalListener((key, value, cause) -> {
                    log.info("remove caffeine cache key:{} value:{} cause:{}", key, value, cause);
                });
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheNames(caffeineCacheProperties.getNames());
        return cacheManager;
    }

    /**
     * 必须要指定这个Bean，refreshAfterWrite这个配置属性才生效
     *
     * @return
     */
    @Bean
    public CacheLoader<Object, Object> cacheLoader() {
        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                return null;
            }
            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(Object key, Object oldValue) {
                return oldValue;
            }
        };
        return cacheLoader;
    }
}
