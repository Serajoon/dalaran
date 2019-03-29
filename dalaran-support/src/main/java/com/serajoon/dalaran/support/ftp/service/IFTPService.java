package com.serajoon.dalaran.support.ftp.service;

import com.serajoon.dalaran.common.web.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IFTPService {
    /**
     * 上传
     * @param multipartFileList multipartFileList
     * @param request request
     * @return
     */
    List<ResponseResult> upload(List<MultipartFile> multipartFileList, HttpServletRequest request);

    /**
     * 下载
     * @param pathname 路径
     * @param filename 文件名
     * @param realName 原始文件名
     * @param response response
     */
    void download(String pathname, String filename, String realName, HttpServletResponse response);

    /**
     * 删除文件
     * @param id file id
     * @param pathname 路径名
     * @param filename 文件名
     * @return ResponseResult
     */
    ResponseResult deleteFile(String id, String pathname, String filename);

}
