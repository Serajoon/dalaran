package com.serajoon.dalaran.support.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * 国际化和controller返回值Service
 * @author  hanmeng
 * @since  2019/4/24 8:37
 */
@Component
public class LocaleMessage {

    @Resource
    private MessageSource messageSource;

    @Resource
    private Locale locale;

    /**
     * @param code ：对应messages配置的key.
     * @return 返回值信息
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @return 返回值信息
     */
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, "");
    }


    /**
     * @param code           ：对应messages配置的key.
     * @param args           : 数组参数.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return 返回值信息
     */
    public String getMessage(String code, Object[] args, String defaultMessage) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

}
