package com.serajoon.dalaran.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 参数异常
 *
 * @author hanmeng
 * @since 2019/5/6 9:55
 */
@Getter
@Setter
public class ParamException extends BaseException {
    private static final long serialVersionUID = -5910726845769261362L;
    private String msg;

    public ParamException(String message) {
        this.msg = message;
    }
}