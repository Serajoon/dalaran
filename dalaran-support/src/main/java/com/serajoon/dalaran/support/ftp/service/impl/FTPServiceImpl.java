package com.serajoon.dalaran.support.ftp.service.impl;

import com.google.common.base.Joiner;
import com.serajoon.dalaran.common.constants.MyCharset;
import com.serajoon.dalaran.common.enums.FileUpLoadEnum;
import com.serajoon.dalaran.common.generator.IdGenerator;
import com.serajoon.dalaran.common.util.MyDateTimeUtils;
import com.serajoon.dalaran.common.util.MyNetUtils;
import com.serajoon.dalaran.common.util.MyStringUtils;
import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.ftp.config.FTPProperties;
import com.serajoon.dalaran.support.ftp.dao.FileuploadDao;
import com.serajoon.dalaran.support.ftp.model.Fileupload;
import com.serajoon.dalaran.support.ftp.pool.FTPClientPool;
import com.serajoon.dalaran.support.ftp.service.IFTPService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FTP service
 *
 * @author hm 2019/1/29 20:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class FTPServiceImpl implements IFTPService {

    private static final String FTP_PATH_SEPARATOR = "/";

    private static final String CONTENTTYPE = "application/octet-stream;charset=utf-8";

    @Resource
    private FTPProperties ftpProperties;

    @Resource
    private FileuploadDao fileuploadDao;

    @Resource
    private FTPClientPool ftpPool;

    @Deprecated
    public FTPClient ftpClient() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(MyCharset.DEFAULT_CHARSET_TEXT);
        ftpClient.setConnectTimeout(ftpProperties.getTimeout());
        try {
            log.info("连接{}FTP服务器，端口{}", ftpProperties.getHost(), ftpProperties.getPort());
            ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());
            ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
            ftpClient.setDefaultTimeout(ftpProperties.getTimeout());
            int replyCode = ftpClient.getReplyCode();
            if (FTPReply.isPositiveCompletion(replyCode)) {
                log.info("连接FTP成功");
            } else {
                log.error("连接FTP服务器失败");
            }
        } catch (IOException e) {
            log.error("连接FTP服务器失败");
        }
        return ftpClient;
    }

    /**
     * 判断FTP服务是否可用
     *
     * @return boolean 是否可用
     * @author hanmeng
     */
    public boolean reachable(FTPClient ftpClient) {
        boolean result = false;
        //服务可用性判断
        if (MyNetUtils.isIpAndPortReachable(ftpProperties.getHost(), ftpProperties.getPort())) {
            try {
                if (!ftpClient.isConnected()) {
                    ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());
                    ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
                }
                if (ftpClient.isConnected()) {
                    result = true;
                }
            } catch (IOException e) {
                log.error("登录FTP失败");
                e.printStackTrace();
            }
        } else {
            log.error("服务不可用");
        }
        return result;
    }

    /**
     * FTP上传文件
     *
     * @param fileupload  上传文件信息
     * @param inputStream 输入文件流
     * @return ResponseResult
     */
    public ResponseResult doUpload(Fileupload fileupload, InputStream inputStream) {
        ResponseResult result;
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpPool.borrowObject();
            System.out.println(ftpClient);
            if (!reachable(ftpClient)) {
                throw new ConnectException("连接FTP服务失败");
            }
            String pathname = fileupload.getLocation();
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                createDirecroty(pathname, ftpClient);
                ftpClient.makeDirectory(pathname);
                ftpClient.changeWorkingDirectory(pathname);
                ftpClient.storeFile(fileupload.getFileNewName(), bufferedInputStream);
                /*ftpClient.logout();*/
                fileuploadDao.insert(fileupload);
                result = ResponseResult.build().success(fileupload, "上传成功");
            }
        } catch (Exception e) {
            // 出现异常从FTP池中移除有问题的ftpClient
            releaseFTPClientWithException(ftpClient);
            log.error("{} {}", "上传文件失败", fileupload);
            e.printStackTrace();
            result = ResponseResult.build().failed("上传FTP失败");
        } finally {
//            releaseFTPClientWithOutPendingCommand(ftpClient);
            releaseFTPClientWithPendingCommand(ftpClient);
        }
        return result;
    }

    @Override
    public List<ResponseResult> upload(List<MultipartFile> multipartFileList, HttpServletRequest request) {
        return multipartFileList.parallelStream()
                .filter(t -> StringUtils.hasLength(t.getOriginalFilename()))
                .map(multipartFile -> {
                    Fileupload fileupload = getFileupload(multipartFile, request);
                    ResponseResult responseResult = null;
                    try (InputStream inputStream = multipartFile.getInputStream()) {
                        responseResult = doUpload(fileupload, inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return responseResult;
                }).collect(Collectors.toList());
    }

    /**
     * @param pathname 路径名
     * @param filename 文件名
     * @param realName 原文件名
     * @author hanmeng1
     * @since 2019/2/21 19:11
     */
    @Override
    public void download(String pathname, String filename, String realName, HttpServletResponse response) {
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpPool.borrowObject();
            if (!reachable(ftpClient)) {
                throw new ConnectException("连接FTP服务失败");
            }
            log.info("开始下载文件{}/{}", pathname, filename);
            // 设置文件类型为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles(pathname);
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    try (InputStream inputStream = ftpClient.retrieveFileStream(pathname + FTP_PATH_SEPARATOR + filename)) {
                        if (Objects.nonNull(inputStream)) {
                            try (BufferedInputStream input = new BufferedInputStream(inputStream);
                                 BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
                                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realName, MyCharset.DEFAULT_CHARSET_TEXT));
                                response.setContentType(CONTENTTYPE);
                                byte[] bytes = new byte[1024];
                                int n;
                                while ((n = input.read(bytes)) != -1) {
                                    output.write(bytes, 0, n);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("下载文件失败");
            e.printStackTrace();
            releaseFTPClientWithException(ftpClient);
        } finally {
            releaseFTPClientWithPendingCommand(ftpClient);
        }
    }

    /**
     * 创建FTP目录
     *
     * @param dir 目录
     * @return true:成功 false:失败
     * @throws IOException IOException
     */
    private boolean makeDirectory(String dir, FTPClient ftpClient) throws IOException {
        return ftpClient.makeDirectory(dir);
    }

    /**
     * 创建FTP多层目录文件，如果有ftp服务器已存，则不创建，否则，则创建
     */
    private void createDirecroty(String remote, FTPClient ftpClient) throws IOException {
        String directory = remote + FTP_PATH_SEPARATOR;
        // 如果远程目录不存在,则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase(FTP_PATH_SEPARATOR) && !changeWorkingDirectory(directory, ftpClient)) {
            int start;
            int end;
            if (directory.startsWith(FTP_PATH_SEPARATOR)) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf(FTP_PATH_SEPARATOR, start);
            String path = MyStringUtils.EMPTY;
            StringBuilder paths = new StringBuilder(MyStringUtils.EMPTY);
            for (; ; ) {
                String subDirectory = new String(remote.substring(start, end).getBytes(MyCharset.DEFAULT_CHARSET.name()), MyCharset.DEFAULT_CHARSET.name());
                path = path + FTP_PATH_SEPARATOR + subDirectory;
                if (!existFile(path, ftpClient)) {
                    if (makeDirectory(subDirectory, ftpClient)) {
                        changeWorkingDirectory(subDirectory, ftpClient);
                    } else {
                        log.error("创建目录{}失败", subDirectory);
                        changeWorkingDirectory(subDirectory, ftpClient);
                    }
                } else {
                    changeWorkingDirectory(subDirectory, ftpClient);
                }
                paths.append(FTP_PATH_SEPARATOR).append(subDirectory);
                start = end + 1;
                end = directory.indexOf(FTP_PATH_SEPARATOR, start);
                if (end <= start) {
                    break;
                }
            }
        }
    }


    /**
     * 改变目录路径
     *
     * @param directory 路径
     * @param ftpClient ftpClient
     * @return 是否切换成功
     */
    private boolean changeWorkingDirectory(String directory, FTPClient ftpClient) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                log.info("进入文件夹{}成功", directory);
            } else {
                log.info("进入文件夹{}失败！开始创建文件夹", directory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 判断ftp服务器文件是否存在
     *
     * @param path 路径
     * @return Boolean true:存在 false:不存在
     * @author hanmeng
     * @since 2019/1/25 18:43
     */
    private boolean existFile(String path, FTPClient ftpClient) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (!ObjectUtils.isEmpty(ftpFileArr)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 删除FTP文件
     *
     * @param id       表主键
     * @param pathname 路径
     * @param filename 文件名
     * @return ResponseResult responseResult
     * @author hanmeng
     * @since 2019/1/25 18:41
     */
    @Override
    public ResponseResult deleteFile(String id, String pathname, String filename) {
        ResponseResult responseResult;
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpPool.borrowObject();
            if (!reachable(ftpClient)) {
                return ResponseResult.build().failed("连接FTP服务失败");
            }
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            responseResult = ResponseResult.build().success("删除文件成功");
            fileuploadDao.deleteById(id);
        } catch (Exception e) {
            responseResult = ResponseResult.build().failed("删除文件失败");
            releaseFTPClientWithException(ftpClient);
            e.printStackTrace();
        } finally {
            releaseFTPClientWithOutPendingCommand(ftpClient);
        }
        return responseResult;
    }

    /**
     * 正常上传完成释放连接
     *
     * @param ftpClient FTP客户端对象
     */
    private void releaseFTPClientWithOutPendingCommand(FTPClient ftpClient) {
        if (Objects.nonNull(ftpClient)) {
            ftpPool.returnObject(ftpClient);
        }
    }

    /**
     * 下载完成时,正常释放ftp连接
     *
     * @param ftpClient FTP客户端对象
     */
    private void releaseFTPClientWithPendingCommand(FTPClient ftpClient) {
        ftpPool.returnObject(ftpClient);
        try {
            if (ftpClient.isConnected()) {
                ftpClient.completePendingCommand();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常时 断开FTP连接 将异常的FTPClient移除连接池
     *
     * @param ftpClient FTP客户端对象
     */
    private void releaseFTPClientWithException(FTPClient ftpClient) {
        try {
            //从连接池中移除异常的连接
            ftpPool.invalidateObject(ftpClient);
            if (ftpClient.isConnected()) {
                //断开连接
                ftpClient.disconnect();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 将multipartFile对象转换成Fileupload
     *
     * @param multipartFile 前端触底的multipartFile对象
     * @return request request
     * @author hanmeng1
     * @since 2019/3/29 8:50
     */
    private Fileupload getFileupload(MultipartFile multipartFile, HttpServletRequest request) {
        //根路径名
        String contextPath = request.getContextPath().replaceFirst(FTP_PATH_SEPARATOR, MyStringUtils.EMPTY);
        String contentType = multipartFile.getContentType();
        //获取原始文件名
        String originalFilename = multipartFile.getOriginalFilename();
        String suffixName = originalFilename != null ? Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".")) : null;
        //获取文件类型
        String fileType = FilenameUtils.getExtension(originalFilename);
        //生成新的文件名
        String newFileName = IdGenerator.randomUUID() + suffixName;
        String remoteAddr = request.getRemoteAddr();
        String ftpPath = Joiner.on(FTP_PATH_SEPARATOR).join(ftpProperties.getPath(), MyDateTimeUtils.getCurrentYear(), MyDateTimeUtils.getCurrentMonth(), MyDateTimeUtils.getCurrentDay());
        return Fileupload.builder().contentType(contentType).contextPath(contextPath)
                .fileType(fileType).fileOldName(originalFilename)
                .fileNewName(newFileName).createTime(MyDateTimeUtils.getCurrentDateTimeStr())
                .userId("").userIp(remoteAddr).location(FilenameUtils.normalize(ftpPath, true))
                .serverType(FileUpLoadEnum.FTP.value()).id(IdGenerator.randomUUID()).build();
    }
}
