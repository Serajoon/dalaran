package com.serajoon.dalaran.support.ftp.service;

import com.serajoon.dalaran.common.web.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IFTPService {

    List<ResponseResult> upload(List<MultipartFile> multipartFileList, HttpServletRequest request);

    void download(String pathname, String filename, String realName, HttpServletResponse response) throws IOException;

    ResponseResult deleteFile(String id, String pathname, String filename);
}
