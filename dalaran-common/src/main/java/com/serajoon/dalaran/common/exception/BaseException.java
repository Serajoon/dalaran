package com.serajoon.dalaran.common.exception;
/**
 *
 * @author  hanmeng
 * @since  2019/5/6 9:47
 */
public class BaseException extends Exception {
    private static final long serialVersionUID = -2570032622341138913L;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
