package com.serajoon.dalaran.common.web.response;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller返回
 *
 * @author hm 2019/1/6 22:35
 */
@Getter
@Setter
public class ResponseResult {

    /**成功
     * 200
     */
    public static final int SUCCESS = HttpStatus.OK.value();
    /** 未认证
     * 401
     */
    public static final int UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();
    /** 禁止
     * 403
     */
    public static final int FORBIDDEN = HttpStatus.FORBIDDEN.value();
    /**
     * 没有发现资源
     * 404
     */
    public static final int VALIDATE_FAILED = HttpStatus.NOT_FOUND.value();
    /**
     * 失败
     * 500
     */
    public static final int FAILED = HttpStatus.INTERNAL_SERVER_ERROR.value();

    private static final String SUCCESS_MSG = "success";

    private static final String FAILED_MSG = "failed";

    private int code;

    private String message;

    private Object data;

    private ResponseResult() {
    }
    /**
     * 返回对象
     * @author hm 2019/1/6 22:52
    */
    public static ResponseResult build() {
        return new ResponseResult();
    }


    /**
     * 普通成功返回
     *
     * @param data 返回的数据
     * @author hm 2019/1/6 22:36
     */
    public ResponseResult success(Object data) {
        this.code = SUCCESS;
        this.message = SUCCESS_MSG;
        this.data = data;
        return this;
    }

    /**
     * 普通成功返回
     *
     * @param message 返回的结果信息
     * @author hm 2019/1/6 22:36
     */
    public ResponseResult success(Object data, String message) {
        success(data);
        this.code = SUCCESS;
        this.message = message;
        return this;
    }


    /**
     * 返回分页成功数据
     *
     * @param data 分页数据列表信息
     * @author hm 2019/1/6 22:38
     */
    public ResponseResult pageSuccess(List data) {
        PageInfo pageInfo = new PageInfo(data);
        Map<String, Object> result = new HashMap<>();
        result.put("pageNum", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());
        result.put("totalPage", pageInfo.getPages());
        result.put("total", pageInfo.getTotal());
        result.put("list", pageInfo.getList());
        this.code = SUCCESS;
        this.message = SUCCESS_MSG;
        this.data = result;
        return this;
    }

    /**
     * 普通失败提示信息
     *
     * @author hm 2019/1/6 22:43
     */
    public ResponseResult failed() {
        this.code = FAILED;
        this.message = FAILED_MSG;
        return this;
    }

    /**
     * 参数验证失败使用
     *
     * @param message 错误信息
     */
    public ResponseResult validateFailed(String message) {
        this.code = VALIDATE_FAILED;
        this.message = message;
        return this;
    }

    /**
     * 未登录时使用
     *
     * @param message 错误信息
     */
    public ResponseResult unauthorized(String message) {
        this.code = UNAUTHORIZED;
        this.message = "暂未登录或token已经过期";
        this.data = message;
        return this;
    }

    /**
     * 未授权时使用
     *
     * @param message 错误信息
     */
    public ResponseResult forbidden(String message) {
        this.code = FORBIDDEN;
        this.message = "没有相关权限";
        this.data = message;
        return this;
    }

}
