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
 *   # redis过期时间设置,默认是1天
 *   time-to-live: 1h
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
     * redis过期时间,默认1天
     */
    private Duration timeToLive = Duration.ofDays(1);

    /**
     *
     */
    private Map<String, Duration> favorite;


}
