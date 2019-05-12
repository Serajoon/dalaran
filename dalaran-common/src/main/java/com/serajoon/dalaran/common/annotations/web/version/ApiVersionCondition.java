package com.serajoon.dalaran.common.annotations.web.version;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 匹配ApiVersion版本
 * 匹配过滤出当前访问接口中是否存在v[Num],如果存在继续判断,如果不存在,报错
 * 请求的版本值大于控制器中版本值，则取控制器版本中最大的
 *
 * @author hm 2019/1/9 7:14
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    // 路径中版本的前缀， 这里用 /v[1-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");

    /**
     * api的版本
     */
    private int apiVersion;

    public ApiVersionCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }


    /**
     * 将不同的筛选条件合并,采用最后定义优先原则，则方法上的定义覆盖类上面的定义
     *
     * @param apiVersionCondition
     * @return
     */
    @Override
    public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
        return new ApiVersionCondition(apiVersionCondition.getApiVersion());
    }


    /**
     * 根据request查找匹配到的筛选条件
     * 如果传入的版本号大于等于配置的版本号,则返回配置的版本号
     * 如果传入的版本号小于配置的版本号,则匹配失败
     *
     * @param httpServletRequest httpServletRequest
     * @return ApiVersionCondition
     */
    @Nullable
    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(httpServletRequest.getRequestURI());
        if (m.find()) {
            //取第一个匹配
            Integer version = Integer.valueOf(m.group(1));
            if (version >= this.apiVersion) {
                // 如果请求的版本号大于等于配置版本号,则满足
                return this;
            }
        }
        return null;
    }

    /**
     * 不同筛选条件比较,用于排序,优先匹配最新的版本号
     *
     * @param apiVersionCondition 自定义ApiVersionCondition对象
     * @param httpServletRequest  httpServletRequest
     * @return
     */
    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        return apiVersionCondition.getApiVersion() - this.apiVersion;
    }

    public int getApiVersion() {
        return apiVersion;
    }


}
