package com.serajoon.dalaran.support.cache.rediscache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

/**
 * application.ymlredis属性配置
 * <pre>
 * rediscache:
 *   # redis过期时间设置,默认是30分钟
 *   # 1小时
 *   time-to-live: 1h
 *   # 5分钟
 *   customTimeToLive:
 *     config-user: 5m
 * </pre>
 *
 * @author hanmeng
 * @since 2019/4/4 14:28
 */
@Getter
@Setter
@ConfigurationProperties(prefix = RedisCacheProperties.REDISCACHE_PREFIX)
public class RedisCacheProperties {

    static final String REDISCACHE_PREFIX = "rediscache";

    /**
     * 不配置的话,默认redis过期时间,默认30分钟
     */
    private Duration timeToLive = Duration.ofMinutes(30);

    /**
     * 自定义缓存过期时间
     */
    private Map<String, Duration> customTimeToLive;


}
