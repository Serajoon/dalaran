package com.serajoon.dalaran.support.cache.rediscache;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j
public class CallbackCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(@NotNull RuntimeException exception, @NotNull Cache cache, @NotNull Object key) {
        log.error("cache get error, cacheName:{}, key:{}, msg:", cache.getName(), key, exception);
    }

    @Override
    public void handleCachePutError(@NotNull RuntimeException exception, @NotNull Cache cache, @NotNull Object key, Object value) {
        log.error("cache put error, cacheName:{}, key:{}, msg:", cache.getName(), key, exception);

    }

    @Override
    public void handleCacheEvictError(@NotNull RuntimeException exception, @NotNull Cache cache, @NotNull Object key) {
        log.error("cache evict error, cacheName:{}, key:{}, msg:", cache.getName(), key, exception);

    }

    @Override
    public void handleCacheClearError(@NotNull RuntimeException exception, @NotNull Cache cache) {
        log.error("cache clear error, cacheName:{}, msg:", cache.getName(), exception);
    }
}