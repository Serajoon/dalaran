package com.serajoon.dalaran.support.spring;

import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.i18n.LocaleMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * controller返回值统一处理
 *
 * @author hanmeng
 * @since 2019/5/5 10:17
 */
@ControllerAdvice
public class ResponseResultAdvice implements ResponseBodyAdvice<ResponseResult> {

    @Resource
    private LocaleMessage localeMessage;

    /**
     * 类型判断，对于符合条件的controller返回值进行统一处理
     */
    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return ResponseResult.class.isAssignableFrom(((Method) returnType.getExecutable()).getReturnType());
    }

    /**
     * 统一处理
     */
    @Override
    public ResponseResult beforeBodyWrite(ResponseResult body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType,
                                          @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NotNull ServerHttpRequest request,
                                          @NotNull ServerHttpResponse response) {
        Optional.ofNullable(body.getMessage()).ifPresent(t->{
            String message = localeMessage.getMessage(t);
            if(StringUtils.hasText(message)){
                body.setMessage(message);
            }
        });
        return body;
    }

}