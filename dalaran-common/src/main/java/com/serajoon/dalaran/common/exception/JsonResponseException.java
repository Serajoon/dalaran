package com.serajoon.dalaran.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * JSON异常处理
 * @author  hanmeng
 * @since  2019/5/6 9:43
 */
@Getter
@Setter
public class JsonResponseException extends BaseException {

    private static final long serialVersionUID = 5016762804129019643L;
    private int status = 500;
    private String message = "unknown exception";

    public JsonResponseException() {
    }

    public JsonResponseException(String message) {
        this.message = message;
    }

    public JsonResponseException(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public JsonResponseException(int status, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.status = status;
    }

    public JsonResponseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public JsonResponseException(int status, Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
        this.status = status;
    }

    public JsonResponseException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }
}