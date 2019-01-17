package com.serajoon.dalaran.common.annotations.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * 函数执行时间监控
 *
 * @author hanmeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {
    /**
     * 函数功能说明 加载controller或者service上
     *
     * @author hm 2019/1/13 17:49
     */
    String value() default "";

    /**
     * 最大容忍时间(秒)，超过则打印warn log
     */
    int max() default 2;

    /**
     * 注解是否可用
     *
     * @return
     */
    boolean enable() default true;
}