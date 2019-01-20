package com.serajoon.config.web.swagger;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * Swagger2API文档的配置
 * // http://ip:port/swagger-ui.html
 *
 * @author hm 2019/1/7 18:27
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enabled")
@EnableConfigurationProperties(SwaggerProperties.class)
@Import({Swagger2DocumentationConfiguration.class})
public class Swagger2Configuration {

    @Autowired
    private SwaggerProperties swaggerProperties;


    /**
     * RequestHandlerSelectors.basePackage(swaggerbasePackage):设置扫描的包路径
     * RequestHandlerSelectors.withClassAnnotation(Api.class):设置标有@Api注解的类,存在注解约束有限选择注解扫描
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any());//设置请求的统一前缀
        if (swaggerProperties.getClassAnnotation()) {
            apiSelectorBuilder.apis(RequestHandlerSelectors.withClassAnnotation(Api.class));
            return apiSelectorBuilder.build();
        }
        return apiSelectorBuilder.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .build();
    }

}
