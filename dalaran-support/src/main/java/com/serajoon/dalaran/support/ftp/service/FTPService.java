package com.serajoon.dalaran.support.ftp.service;


import com.serajoon.dalaran.common.constants.MyCharset;
import com.serajoon.dalaran.common.util.MyHttpUtils;
import com.serajoon.dalaran.common.util.MyStringUtils;
import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.ftp.config.FTPProperties;
import com.serajoon.dalaran.support.ftp.dao.FileuploadDao;
import com.serajoon.dalaran.support.ftp.model.Fileupload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FTP service
 *
 * @author hm 2019/1/29 20:00
 */
//TODO 提示信息
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class FTPService {

    private static final String FTP_PATH_SEPARATOR = "/";

    @Resource
    private FTPProperties ftpProperties;


    @Resource
    private FileuploadDao fileuploadDao;

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
     * @return boolean
     * @author hm
     */
    public boolean isReachable(FTPClient ftpClient) {
        boolean result = false;
        if (MyHttpUtils.isHostConnectable(ftpProperties.getHost(), ftpProperties.getPort())) {//服务可用
            try {
                if (!ftpClient.isConnected()) {
                    ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());
                    ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
                }
                result = true;
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
    @Transactional
    public ResponseResult upload(Fileupload fileupload, InputStream inputStream) {
        FTPClient ftpClient = ftpClient();
        if (!isReachable(ftpClient)) {
            return ResponseResult.build().failed("连接FTP服务失败");
        }
        ResponseResult result;
        String pathname = fileupload.getLocation();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            createDirecroty(pathname, ftpClient);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileupload.getFileNewName(), bufferedInputStream);
            ftpClient.logout();
            fileuploadDao.insert(fileupload);
            result = ResponseResult.build().success(fileupload, "上传成功");
        } catch (Exception e) {
            log.error("{} {}", "上传文件失败", fileupload);
            e.printStackTrace();
            result = ResponseResult.build().failed("上传FTP失败");
        } finally {
            releaseFTPClient(ftpClient);
        }
        return result;
    }


    public InputStream download(String pathname, String filename) {
        FTPClient ftpClient = ftpClient();
        if (!isReachable(ftpClient)) {
            log.error("连接FTP服务失败");
            return null;
        }
        InputStream inputStream = null;
        try {
            log.info("开始下载文件{}/{}", pathname, filename);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    inputStream = ftpClient.retrieveFileStream(pathname + "/" + filename);
                }
            }
            log.info("下载文件{}/{}成功", pathname, filename);
        } catch (Exception e) {
            log.error("下载文件失败");
            e.printStackTrace();
        } finally {
            releaseFTPClient(ftpClient);
        }
        return inputStream;
    }

    /**
     * 创建FTP目录
     *
     * @param dir
     * @return
     * @throws IOException
     */
    private boolean makeDirectory(String dir, FTPClient ftpClient) throws IOException {
        return ftpClient.makeDirectory(dir);
    }

    /**
     * 创建FTP多层目录文件，如果有ftp服务器已存，则不创建，否则，则创建
     *
     * @param remote
     * @throws IOException
     */
    private void createDirecroty(String remote, FTPClient ftpClient) throws IOException {
        String directory = remote + FTP_PATH_SEPARATOR;
        // 如果远程目录不存在,则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(directory, ftpClient)) {
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
            while (true) {
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

    //改变目录路径
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
        if (ftpFileArr.length > 0) {
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
    public ResponseResult deleteFile(String id, String pathname, String filename) {
        ResponseResult responseResult;
        FTPClient ftpClient = ftpClient();
        if (!isReachable(ftpClient)) {
            responseResult = ResponseResult.build().failed("连接FTP服务失败");
        }
        try {
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            responseResult = ResponseResult.build().success("删除文件成功");
            fileuploadDao.deleteById(id);
        } catch (Exception e) {
            responseResult = ResponseResult.build().failed("删除文件失败");
            e.printStackTrace();
        } finally {
            releaseFTPClient(ftpClient);
        }
        return responseResult;
    }

    private void releaseFTPClient(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}