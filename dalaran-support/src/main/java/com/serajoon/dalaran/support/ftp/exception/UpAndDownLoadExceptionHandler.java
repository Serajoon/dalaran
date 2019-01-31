package com.serajoon.dalaran.support.ftp.exception;

import com.serajoon.dalaran.common.web.response.ResponseResult;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上传下载异常返回信息类
 *
 * <p>
 * application.yml
 * <pre>
 * spring:
 *   servlet:
 *     multipart:
 *       enabled: true
 *       max-file-size:  11MB # 最大11MB,-1:无限制
 *       max-request-size: -1 # 无限制
 * </pre>
 *
 * @author hanmeng
 * @since 2019/1/28 15:40
 */
@RestControllerAdvice
public class UpAndDownLoadExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MultipartException.class})
    public ResponseResult handleMultipartException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        //TODO 错误提示提取出来
        String message;
        MultipartException mEx = (MultipartException) ex;
        Throwable cause = ex.getCause().getCause();
        if (cause instanceof FileUploadBase.SizeLimitExceededException) {
            FileUploadBase.SizeLimitExceededException flEx = (FileUploadBase.SizeLimitExceededException) cause;
            float permittedSize = flEx.getPermittedSize() / 1024F / 1024F;
            message = "下载的文件不能超过" + permittedSize + "MB";
        } else if (cause instanceof FileUploadBase.FileSizeLimitExceededException) {
            FileUploadBase.FileSizeLimitExceededException flEx = (FileUploadBase.FileSizeLimitExceededException) mEx.getCause().getCause();
            float permittedSize = flEx.getPermittedSize() / 1024F / 1024F;
            message = "上传的文件不能超过" + permittedSize + "MB";
        } else {
            message = "请联系管理员: " + ex.getMessage();
        }
        return ResponseResult.build().failed(message);
    }
}