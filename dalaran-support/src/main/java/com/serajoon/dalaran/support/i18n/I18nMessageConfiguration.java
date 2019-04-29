package com.serajoon.dalaran.support.i18n;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

/**
 * 国际化配置类
 * <pre>
 * spring:
 *   messages:
 *     # 表示放在classpath的i18n文件夹，文件前缀为messages
 *     basename: i18n/messages
 *     i18n: jp_JP
 * </pre>
 * @author  hanmeng
 * @since  2019/4/24 8:50
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.messages")
@Getter
@Setter
public class I18nMessageConfiguration {
    /**
     * 默认是zh_CN
     */
    private String i18n = Locale.SIMPLIFIED_CHINESE.toString();

    @Bean
    public Locale myLocale() {
        return new Locale(i18n);
    }
}
