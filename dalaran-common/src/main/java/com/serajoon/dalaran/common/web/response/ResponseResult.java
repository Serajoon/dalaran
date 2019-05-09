package com.serajoon.dalaran.common.web.response;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
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
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = -4219517900431010825L;

    /**
     * 成功
     * 200
     */
    public static final int SUCCESS = HttpStatus.OK.value();
    /**
     * 未认证
     * 401
     */
    public static final int UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();
    /**
     * 禁止
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

    /**
     * 默认的成功信息
     */
    private static final String SUCCESS_MSG = "success";

    /**
     * 默认的失败信息
     */
    private static final String FAILED_MSG = "failed";


    /**
     * 状态码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 页号
     */
    private static final String PAGE_NUM = "pageNum";

    /**
     * 每页展示的条数
     */
    private static final String PAGE_SIZE = "pageSize";

    /**
     * 总页数
     */
    private static final String TOTAL_PAGE = "totalPage";

    /**
     * 总条数
     */
    private static final String TOTAL = "total";

    /**
     * 列表数据
     */
    private static final String LIST = "list";

    /**
     * 是否成功，默认为 false
     */
    private boolean success = false;

    private ResponseResult() {
    }

    /**
     * 返回ResponseResult对象
     *
     * @author hm 2019/1/6 22:52
     */
    public static <T> ResponseResult<T> build() {
        return new ResponseResult<>();
    }

    /**
     * 普通成功返回
     *
     * @author hm 2019/1/6 22:36
     */
    public static <T> ResponseResult<T> success() {
        return success(null,SUCCESS_MSG);
    }

    /**
     * 普通成功返回
     *
     * @param data 返回的数据
     * @author hm 2019/1/6 22:36
     */
    public static <T> ResponseResult<T> success(T data) {
        return success(data, SUCCESS_MSG);
    }

    /**
     * 普通成功返回
     *
     * @param message 返回的信息
     * @author hm 2019/1/6 22:36
     */
    public static <T> ResponseResult<T> success(String message) {
        return success(null, message);
    }

    /**
     * 普通成功返回
     *
     * @param data    数据
     * @param message 返回的结果信息
     * @author hm 2019/1/6 22:36
     */
    public static <T> ResponseResult<T> success(T data, String message) {
        return success(data, message, SUCCESS);
    }

    /**
     * @param data    数据
     * @param message 返回值
     * @param code    返回状态码
     * @return ResponseResult
     * @author hanmeng1
     * @since 2019/4/30 12:00
     */
    public static <T> ResponseResult<T> success(T data, String message, int code) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setData(data);
        result.setMessage(message);
        result.setCode(code);
        result.setSuccess(true);
        return result;
    }


    /**
     * 返回分页成功数据
     *
     * @param data 分页数据列表信息
     * @author hm 2019/1/6 22:38
     */
    @SuppressWarnings("all")
    public static <T> ResponseResult<T> pageSuccess(List data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        PageInfo pageInfo = new PageInfo<>(data);
        Map<String, Object> result = new HashMap<>(8);
        result.put(PAGE_NUM, pageInfo.getPageNum());
        result.put(PAGE_SIZE, pageInfo.getPageSize());
        result.put(TOTAL_PAGE, pageInfo.getPages());
        result.put(TOTAL, pageInfo.getTotal());
        result.put(LIST, pageInfo.getList());
        responseResult.setCode(SUCCESS);
        responseResult.setMessage(SUCCESS_MSG);
        responseResult.setData((T) result);
        responseResult.setSuccess(true);
        return responseResult;
    }

    /**
     * 普通失败提示信息
     *
     * @author hm 2019/1/6 22:43
     */
    public static <T> ResponseResult<T> failed() {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(FAILED);
        responseResult.setMessage(FAILED_MSG);
        return responseResult;
    }

    /**
     * 普通失败提示信息
     *
     * @author hm 2019/1/6 22:43
     */
    public static <T> ResponseResult<T> failed(String msg) {
        return failed(null, msg);
    }

    /**
     * 带数据的错误返回
     *
     * @return com.hisense.common.web.response.ResponseResult
     * @author xdd
     */
    public static <T> ResponseResult<T> failed(T data, String msg) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(FAILED);
        responseResult.setMessage(msg);
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 参数验证失败使用
     *
     * @param message 错误信息
     */
    public static <T> ResponseResult<T> validateFailed(String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(VALIDATE_FAILED);
        responseResult.setMessage(message);
        return responseResult;
    }

    /**
     * 未登录时使用
     *
     * @param message 错误信息
     */
    public static <T> ResponseResult<T> unauthorized(String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(UNAUTHORIZED);
        responseResult.setMessage(message);
        return responseResult;
    }

    /**
     * 未授权时使用
     *
     * @param message 错误信息
     */
    public static <T> ResponseResult<T> forbidden(String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(FORBIDDEN);
        responseResult.setMessage(message);
        return responseResult;
    }

}
