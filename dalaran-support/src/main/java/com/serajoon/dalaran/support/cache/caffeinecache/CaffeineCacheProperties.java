package com.serajoon.dalaran.support.cache.caffeinecache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * CaffeineCache配置属性读取类,application.yml
 * <pre>
 * caffeinecache:
 *   initialcapacity: 2
 *   maximumsize: 4
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
@ConfigurationProperties(prefix = CaffeineCacheProperties.CAFFEINECACHE_PREFIX)
public class CaffeineCacheProperties {

    static final String CAFFEINECACHE_PREFIX = "caffeinecache";
    /**
     * 初始容量
     */
    private Integer initialcapacity;
    /**
     * 最大容量
     */
    private Integer maximumsize;

    private List<String> names;
}
