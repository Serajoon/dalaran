package com.serajoon.dalaran.support.cache.rediscache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serajoon.dalaran.common.condition.RedisCondition;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisCacheProperties.class)
@Slf4j
@SuppressWarnings({"all"})
public class RedisCacheConfig extends CachingConfigurerSupport {

    private final RedisCacheProperties redisCacheProperties;

    @Autowired
    public RedisCacheConfig(RedisCacheProperties redisCacheProperties) {
        this.redisCacheProperties = redisCacheProperties;
    }


    @Bean
    @Primary
    @Conditional(RedisCondition.class)
    RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(getValueSerializer());
        template.setHashValueSerializer(getValueSerializer());
        template.afterPropertiesSet();
        return template;
    }


    @Bean
    @SuppressWarnings({"all"})
    CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        //自定义的缓存过期时间
        Map<String, Duration> customTimeToLive = redisCacheProperties.getCustomTimeToLive();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        customTimeToLive.forEach((k, v) -> {
            RedisCacheConfiguration redisCacheConfiguration = redisCacheConfigurationFactory(v);
            redisCacheConfigurationMap.put(k.replaceAll("-", ":"), redisCacheConfiguration);
        });
        // 默认的缓存过期时间 默认30分钟
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        // 读取配置文件中配置的缓存过期时间
        RedisCacheConfiguration config = redisCacheConfigurationFactory(redisCacheProperties.getTimeToLive());
        // 初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, config, redisCacheConfigurationMap);
        return cacheManager;
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
        log.info("初始化 -> [{}]", "Redis CacheErrorHandler");
        return new CacheErrorHandler() {
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
        };
    }

    /**
     * 返回redis的值序列化对象
     * @return
     */
    private RedisSerializer getValueSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    private RedisCacheConfiguration redisCacheConfigurationFactory(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .computePrefixWith(name -> name + ":")
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getValueSerializer()))
                .disableCachingNullValues();
    }

}
