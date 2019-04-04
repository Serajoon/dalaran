package com.serajoon.dalaran.support.cache.concurrentmapcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(ConcurrentMapCacheProperties.class)
@EnableConfigurationProperties(ConcurrentMapCacheProperties.class)
public class ConcurrentMapCacheConfig {

    @Autowired
    private ConcurrentMapCacheProperties concurrentMapCacheProperties;

    @Bean
    public CacheManager cacheManagerWithConcurrentMap() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(concurrentMapCacheProperties.getNames());
        return cacheManager;
    }
}
