package com.serajoon.dalaran.support.exception;

import com.google.common.base.Throwables;
import com.serajoon.dalaran.common.exception.BaseException;
import com.serajoon.dalaran.support.i18n.LocaleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 统一异常处理
 *
 * @author hanmeng
 * @since 2019/5/5 15:42
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    private static final String CODE = "code";

    private static final String MESSAGE = "message";

    @Resource
    private LocaleMessage localeMessage;

    /**
     * 自定义异常统一处理
     *
     * @param e 自定义异常
     * @return Map
     * @author hanmeng1
     * @since 2019/5/6 16:28
     */
    @ExceptionHandler(BaseException.class)
    public Map<String, Object> handleMyException(Exception e) {
        Map<String, Object> map = new HashMap<>(8);
        map.put(CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        Optional.ofNullable(e.getMessage()).ifPresent(t -> {
            String message = localeMessage.getMessage(t);
            if (StringUtils.hasText(message)) {
                map.put(MESSAGE, message);
            } else {
                map.put(MESSAGE, t);
                message = t;
            }
            log.error(message);
        });
        return map;
    }

    /**
     * 非自定义异常处理类
     *
     * @author hanmeng
     * @since 2019/5/6 16:29
     */
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        Map<String, Object> map = new HashMap<>(8);
        map.put(CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        Optional.ofNullable(e.getMessage()).ifPresent(msg -> {
            String message = localeMessage.getMessage(msg);
            message = StringUtils.hasText(message) ? message : localeMessage.getMessage("system.error");
            map.put(MESSAGE, message);
            log.error(message, e, Throwables.getStackTraceAsString(e));
        });
        return map;
    }


}
