package com.serajoon.dalaran.support.ftp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * application.yml
 * <pre>
 * ftp:
 *   host: 10.16.6.117
 *   port: 21
 *   path: ${server.servlet.context-path}
 *   username: ftp-1
 *   password: 123456
 *   timeout: 200
 * </pre>
 */
@ConfigurationProperties(prefix = "ftp")
@Getter
@Setter
public class FTPProperties {
    /**
     * FTP服务器IP
     */
    private String host;
    /**
     * FTP服务器端口
     */
    private int port;
    /**
     * FTP用户名
     */
    private String username;
    /**
     * FTP用户密码
     */
    private String password;

    /**
     * FTP连接超时时间(毫秒)
     */
    private int timeout;
    /**
     * FTP根路径
     */
    private String path = "/";

    private int bufferSize = 8096;
    /**
     * 初始化连接数
     */
    private Integer initialSize = 0;

    private String encoding = "UTF-8";


}
