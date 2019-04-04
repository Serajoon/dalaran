package com.serajoon.dalaran.support.cache.concurrentmapcache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * ConcurrentMapCache配置读取类
 * application.yml
 * <pre>
 * concurrentmapcache:
 *   names:
 *   - cache1
 *   - cache2
 *   - shop
 *   - user
 * </pre>
 *
 * @author hanmeng
 * @since 2019/4/4 14:24
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ConcurrentMapCacheProperties.CONCURRENTMAPCACHE_PREFIX)
public class ConcurrentMapCacheProperties {

    static final String CONCURRENTMAPCACHE_PREFIX = "concurrentmapcache";

    private List<String> names;
}

