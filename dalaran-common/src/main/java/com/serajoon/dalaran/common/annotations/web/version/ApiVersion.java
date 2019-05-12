package com.serajoon.dalaran.common.annotations.web.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    /**
     * 自定义RestApi版本号注解,可以标注在类或者方法上,运行时环境使用
     *
     * @return version
     */
    int value();
}
