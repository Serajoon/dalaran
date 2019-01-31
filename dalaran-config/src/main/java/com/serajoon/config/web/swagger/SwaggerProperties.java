package com.serajoon.config.web.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**application.yml
 *
 * swagger:
 *   enabled: true
 *   classAnnotation: false
 *   title: 后端接口API
 *   description: 后端模块API描述
 *   version: 1.0
 *
 * @author : hm
 */
@ConfigurationProperties(prefix = "swagger", ignoreUnknownFields = false)
@Getter
@Setter
public class SwaggerProperties {
    /**
     * 是否开启swagger,默认不开启
     **/
    private Boolean enabled = false;

    /**
     * 标题
     * 例如 后端Swagger API
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本
     */
    private String version;

    /**
     * 默认扫描的包路径,"":扫描全路径
     */
    private String basePackage = "";

    /**
     * 是否启动类注解扫描
     */
    private Boolean classAnnotation = true;
}