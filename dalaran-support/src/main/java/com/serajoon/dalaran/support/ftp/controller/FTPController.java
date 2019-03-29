package com.serajoon.dalaran.support.ftp.controller;

import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.ftp.dto.FtpDTO;
import com.serajoon.dalaran.support.ftp.model.Fileupload;
import com.serajoon.dalaran.support.ftp.service.IFTPBusinessService;
import com.serajoon.dalaran.support.ftp.service.IFTPService;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/support/ftps")
@Api(description = "ftp功能", tags = "FTPController")
public class FTPController {

    @Resource
    private IFTPService ftpService;

    @Resource
    private IFTPBusinessService ftpBusinessService;

    /**
     * FTP上传接口
     *
     * @param multipartFileList multipartFileList
     * @param request           Request
     * @return List<ResponseResult> 返回结果
     */
    @PostMapping(consumes = "multipart/*", headers = "content-type=multipart/form-data", produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<ResponseResult> upload(@RequestParam("file") List<MultipartFile> multipartFileList, HttpServletRequest request) {
        return ftpService.upload(multipartFileList, request);
    }

    /**
     * 删除FTP文件
     * http://localhost:8000/em/api/support/ftps
     *
     * @param id       file_id
     * @param pathName location
     * @param fileName file_new_name
     * @return
     */
    @DeleteMapping
    @ResponseBody
    public ResponseResult delete(String id, String pathName, String fileName) {
        ResponseResult responseResult;
        try {
            if (StringUtils.isEmpty(id) || StringUtils.isEmpty(pathName) || StringUtils.isEmpty(fileName)) {
                return ResponseResult.build().failed("删除文件失败");
            }
            //删除关联表信息
            FtpDTO ftpDTO = FtpDTO.builder().fileId(id).build();
            ftpBusinessService.delete(ftpDTO);
            //删除附件和附件表信息
            responseResult = ftpService.deleteFile(id, pathName, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult = ResponseResult.build().failed("删除文件失败");
        }
        return responseResult;
    }

    /**
     * http://localhost:8000/em/api/support/ftps?pathName=/em/2019/1/29&fileName=23c51dadf69e4b188a614d89193e26ae.png&realName=1.png
     * FTP下载
     *
     * @param pathName 路径名
     * @param fileName 文件名
     * @param realName 原始文件名
     * @param response 返回
     * @throws IOException IO异常
     */
    @GetMapping
    public void download(String pathName, String fileName, String realName, HttpServletResponse response) throws IOException {
        ftpService.download(pathName, fileName, realName, response);
    }

    /**
     * 业务功能附件查询展示
     *
     * @param businessId 业务id
     * @param type       业务类型
     */
    @GetMapping(value = "/dev")
    public ResponseResult selectFiles(@RequestParam String businessId, String type) {
        List<String> byBussinessIdList = ftpBusinessService.findFileIdByBussinessId(businessId, type);
        List<Fileupload> fileuploadList = byBussinessIdList.parallelStream()
                .map(t -> ftpBusinessService.selectFileById(t))
                .collect(Collectors.toList());
        return ResponseResult.build().success(fileuploadList);
    }
}
