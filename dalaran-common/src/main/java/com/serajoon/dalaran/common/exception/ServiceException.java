package com.serajoon.dalaran.common.exception;
/**
 * Service端一般异常
 * @author  hanmeng
 * @since  2019/5/6 10:20
 */
public class ServiceException extends BaseException {
    private static final long serialVersionUID = 657378777056762471L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}