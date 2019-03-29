package com.serajoon.dalaran.support.ftp.pool;

import com.serajoon.dalaran.common.constants.MyCharset;
import com.serajoon.dalaran.support.ftp.config.FTPProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

/**
 * FTPClient工厂类
 *
 * @author hanmeng
 * @since 2019/3/20 13:52
 */
@Component
@Slf4j
public class FTPClientFactory extends BasePooledObjectFactory<FTPClient> {

    @Resource
    private FTPProperties ftpProperties;

    @Override
    public FTPClient create() {
        return ftpClient();
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> p) {
        Optional.ofNullable(p.getObject()).ifPresent(t -> {
            if (t.isConnected()) {
                try {
                    t.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        return super.makeObject();
    }

    /**
     * 创建FTP连接对象
     *
     * @return FTPClient ftp对象
     * @author hanmeng1
     * @since 2019/3/29 8:54
     */
    private FTPClient ftpClient() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(MyCharset.DEFAULT_CHARSET_TEXT);
        // 不限制超时时间
        ftpClient.setConnectTimeout(0);
        try {
            log.info("连接{}FTP服务器，端口{}", ftpProperties.getHost(), ftpProperties.getPort());
            ftpClient.setDefaultTimeout(ftpProperties.getTimeout());
            ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());
            ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
        } catch (IOException e) {
            log.error("连接FTP服务器失败");
        }
        return ftpClient;
    }
}