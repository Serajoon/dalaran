package com.serajoon.dalaran.config.web;

import com.github.pagehelper.autoconfigure.PageHelperProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PageHelperConfig {

    /**
     * 配置mybatis的分页插件PageHelperProperties
     *
     * @author hm 2019/1/7 18:57
     */
    @Bean
    @Primary
    public PageHelperProperties pageHelper() {
        PageHelperProperties pageHelperProperties = new PageHelperProperties();
        pageHelperProperties.setAutoRuntimeDialect(Boolean.TRUE.toString());
        //pageHelperProperties.setHelperDialect("mysql");
        //启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页
        pageHelperProperties.setReasonable(Boolean.TRUE.toString());
        //默认值为false，当该参数设置为true时，如果pageSize=0或者RowBounds.limit=0就会查询出全部的结果(相当于没有执行分页查询，但是返回结果仍然是Page类型)
        pageHelperProperties.setPageSizeZero(Boolean.TRUE.toString());
        pageHelperProperties.setParams("count=countSql");
        return pageHelperProperties;
    }
}
