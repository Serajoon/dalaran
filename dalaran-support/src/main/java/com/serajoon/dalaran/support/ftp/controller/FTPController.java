package com.serajoon.dalaran.support.ftp.controller;

import com.google.common.base.Joiner;

import com.serajoon.dalaran.common.enums.FileUpLoadEnum;
import com.serajoon.dalaran.common.generator.IdGenerator;
import com.serajoon.dalaran.common.util.MyDateTimeUtils;
import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.ftp.config.FTPProperties;
import com.serajoon.dalaran.support.ftp.model.Fileupload;
import com.serajoon.dalaran.support.ftp.service.FTPService;
import io.swagger.annotations.Api;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/support")
@Api(description = "FTP", tags = "FTPController")
public class FTPController {

    @Autowired
    private FTPProperties fTPProperties;

    @Autowired
    private FTPService ftpService;

    /**
     * FTP上传接口
     *
     * @param multipartFileList multipartFileList
     * @param request Request
     * @return List<ResponseResult> 返回结果
     */
    @PostMapping(value = "/ftps")
    @ResponseBody
    public List<ResponseResult> upload(@RequestParam("file") List<MultipartFile> multipartFileList, HttpServletRequest request) {
        return multipartFileList.parallelStream()
                .filter(t -> org.springframework.util.StringUtils.hasLength(t.getOriginalFilename()))
                .map(multipartFile -> {
                    Fileupload fileupload = getFileupload(multipartFile, request);
                    ResponseResult responseResult = null;
                    try (InputStream inputStream = multipartFile.getInputStream()) {
                        responseResult = ftpService.upload(fileupload, inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return responseResult;
                }).collect(Collectors.toList());
    }

    /**
     * 删除FTP文件
     * http://localhost:8000/em/api/support/ftps
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
     * @param pathName
     * @param fileName
     * @param realName
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/ftps")
    public void download(String pathName, String fileName, String realName, HttpServletResponse response) throws IOException {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try (InputStream inputStream = ftpService.download(pathName, fileName)) {
            if (inputStream != null) {
                bufferedInputStream = new BufferedInputStream(inputStream);
                bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
                response.setHeader("Content-Disposition", "attachment;filename=" + realName);
                response.setContentType("application/octet-stream;charset=utf-8");
                byte[] bytes = new byte[1024];
                int n;
                while ((n = bufferedInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, n);
                }
            }
        } finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
        }
    }


    private Fileupload getFileupload(MultipartFile multipartFile, HttpServletRequest request) {
        Fileupload fileupload = new Fileupload();
        String contextPath = request.getContextPath().replaceFirst("/", "");//根路径名
        String contentType = multipartFile.getContentType();
        String originalFilename = multipartFile.getOriginalFilename();//获取文件名
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileType = FilenameUtils.getExtension(originalFilename);//获取文件类型
        String newFileName = IdGenerator.randomUUID() + suffixName;//生成新的文件名
        String remoteAddr = request.getRemoteAddr();
        String ftpPath = Joiner.on("/").join(
                fTPProperties.getPath(),
                MyDateTimeUtils.getCurrentYear(),
                MyDateTimeUtils.getCurrentMonth(),
                MyDateTimeUtils.getCurrentDay());
        fileupload.setContentType(contentType);
        fileupload.setContextPath(contextPath);
        fileupload.setFileType(fileType);
        fileupload.setFileOldName(originalFilename);
        fileupload.setFileNewName(newFileName);
        fileupload.setCreateTime(MyDateTimeUtils.getCurrentDateTimeStr());
        fileupload.setUserId("user");
        fileupload.setUserIp(remoteAddr);
        fileupload.setLocation(FilenameUtils.normalize(ftpPath, true));
        fileupload.setServerType(FileUpLoadEnum.FTP.value());
        fileupload.setId(IdGenerator.randomUUID());
        return fileupload;
    }

}
