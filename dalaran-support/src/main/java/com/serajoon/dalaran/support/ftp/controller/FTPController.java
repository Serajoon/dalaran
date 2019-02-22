package com.serajoon.dalaran.support.ftp.controller;

import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.ftp.service.IFTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/support")
public class FTPController {

    @Autowired
    private IFTPService ftpService;

    /**
     * FTP上传接口
     *
     * @param multipartFileList multipartFileList
     * @param request           Request
     * @return List<ResponseResult> 返回结果
     */
    @PostMapping(value = "/ftps")
    @ResponseBody
    public List<ResponseResult> upload(@RequestParam("file") List<MultipartFile> multipartFileList, HttpServletRequest request) {
        return ftpService.upload(multipartFileList,request);
    }

    /**
     * 删除FTP文件
     * http://localhost:8000/em/api/support/ftps
     *
     * @param id
     * @param pathName
     * @param fileName
     * @return
     */
    @DeleteMapping("/ftps")
    @ResponseBody
    public ResponseResult delete(String id, String pathName, String fileName) {
        return ftpService.deleteFile(id, pathName, fileName);
    }

    /**
     * http://localhost:8000/em/api/support/ftps?pathName=/em/2019/1/29&fileName=23c51dadf69e4b188a614d89193e26ae.png&realName=1.png
     *
     * @param pathName
     * @param fileName
     * @param realName
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/ftps")
    public void download(String pathName, String fileName, String realName, HttpServletResponse response) throws IOException {
        ftpService.download(pathName, fileName, realName, response);
    }

}
