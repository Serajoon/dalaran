package com.serajoon.common

import com.github.pagehelper.PageInfo
import org.springframework.http.HttpStatus
import java.util.*

/**
 * Controller返回
 *
 * @author hm 2019/1/6 22:35
 */

class ResponseResultKT {

    var code: Int = 0

    var message: String? = null

    var data: Any? = null

    constructor()


    /**
     * 普通成功返回
     *
     * @param data 返回的数据
     * @author hm 2019/1/6 22:36
     */
    fun success(data: Any): ResponseResultKT {
        this.code = SUCCESS
        this.message = SUCCESS_MSG
        this.data = data
        return this
    }

    /**
     * 普通成功返回
     *
     * @param message 返回的结果信息
     * @author hm 2019/1/6 22:36
     */
    fun success(data: Any, message: String): ResponseResultKT {
        success(data)
        this.code = SUCCESS
        this.message = message
        return this
    }


    /**
     * 返回分页成功数据
     *
     * @param data 分页数据列表信息
     * @author hm 2019/1/6 22:38
     */
    fun pageSuccess(data: List<*>): ResponseResultKT {
        val pageInfo = PageInfo(data)
        val result = HashMap<String, Any>()
        result["pageNum"] = pageInfo.pageNum
        result["pageSize"] = pageInfo.pageSize
        result["totalPage"] = pageInfo.pages
        result["total"] = pageInfo.total
        result["list"] = pageInfo.list
        this.code = SUCCESS
        this.message = SUCCESS_MSG
        this.data = result
        return this
    }

    /**
     * 普通失败提示信息
     *
     * @author hm 2019/1/6 22:43
     */
    fun failed(): ResponseResultKT {
        this.code = FAILED
        this.message = FAILED_MSG
        return this
    }

    /**
     * 参数验证失败使用
     *
     * @param message 错误信息
     */
    fun validateFailed(message: String): ResponseResultKT {
        this.code = VALIDATE_FAILED
        this.message = message
        return this
    }

    /**
     * 未登录时使用
     *
     * @param message 错误信息
     */
    fun unauthorized(message: String): ResponseResultKT {
        this.code = UNAUTHORIZED
        this.message = "暂未登录或token已经过期"
        this.data = message
        return this
    }

    /**
     * 未授权时使用
     *
     * @param message 错误信息
     */
    fun forbidden(message: String): ResponseResultKT {
        this.code = FORBIDDEN
        this.message = "没有相关权限"
        this.data = message
        return this
    }

    companion object {

        /**成功
         * 200
         */
        val SUCCESS = HttpStatus.OK.value()
        /** 未认证
         * 401
         */
        val UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value()
        /** 禁止
         * 403
         */
        val FORBIDDEN = HttpStatus.FORBIDDEN.value()
        /**
         * 没有发现资源
         * 404
         */
        val VALIDATE_FAILED = HttpStatus.NOT_FOUND.value()
        /**
         * 失败
         * 500
         */
        val FAILED = HttpStatus.INTERNAL_SERVER_ERROR.value()

        private val SUCCESS_MSG = "success"

        private val FAILED_MSG = "failed"
    }

}
