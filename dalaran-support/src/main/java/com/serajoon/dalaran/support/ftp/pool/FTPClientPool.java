package com.serajoon.dalaran.support.ftp.pool;

import com.serajoon.dalaran.support.ftp.config.FTPProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FTP连接池
 *
 * @author hanmeng
 * @since 2019/3/20 13:51
 */
@Component
@Slf4j
@Getter
@Setter
public class FTPClientPool extends GenericObjectPool<FTPClient> {

    private FTPClientFactory factory;

    private FTPProperties ftpProperties;

    @Autowired
    public FTPClientPool(FTPClientFactory factory, FTPProperties ftpProperties) {
        super(factory);
        this.factory = factory;
        this.ftpProperties = ftpProperties;
        setTestOnBorrow(true);
        setMaxTotal(ftpProperties.getMaxTotal());
        setMaxIdle(ftpProperties.getMaxIdle());
        setMinIdle(ftpProperties.getMinIdle());
        setMaxWaitMillis(3000);
    }

    @Override
    public FTPClient borrowObject() {
        // 最多循环取10次
        int count = 10;
        FTPClient ftpClient = null;
        for (; ; ) {
            try {
                if (count <= 0) {
                    break;
                }
                ftpClient = super.borrowObject();
                int replyCode = ftpClient.getReplyCode();
                if (FTPReply.isPositiveCompletion(replyCode)) {
                    log.info("连接FTP成功");
                } else {
                    log.error("连接FTP服务器失败");
                }
                ftpClient.getStatus();
                if (!ftpClient.isConnected()) {
                    ftpClient = factory.create();
                }
                break;
            } catch (Exception e) {
                count--;
                try {
                    invalidateObject(ftpClient);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        return ftpClient;
    }
}
