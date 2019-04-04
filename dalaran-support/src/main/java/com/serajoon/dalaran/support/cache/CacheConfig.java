package com.serajoon.dalaran.support.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存key生成
 *
 * @author hanmeng
 * @since 2019/4/4 14:26
 */
@Configuration
public class CacheConfig {
    @Bean
    public KeyGenerator myCustomCacheKeyGenerator() {
        return new CustomCacheKeyGenerator();
    }
}
