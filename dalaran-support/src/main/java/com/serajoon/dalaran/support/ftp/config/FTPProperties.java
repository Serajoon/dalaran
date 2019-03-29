package com.serajoon.dalaran.support.ftp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 读取ftp的配置属性文件
 * <p>
 * application.yml
 * <pre>
 * ftp:
 *   host: 10.16.6.117
 *   port: 21
 *   path: ${server.servlet.context-path}
 *   username: ftp-1
 *   password: 123456
 *   timeout: 200
 *   maxTotal: 50
 *   maxIdle: 10
 *   minIdle: 5
 * </pre>
 * @author hanmeng
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
    /**
     * 缓冲大小
     */
    private int bufferSize = 8096;
    /**
     * 初始化连接数
     */
    private Integer initialSize = 0;

    /**
     * 编码
     */
    private String encoding = "UTF-8";

    /**
     * 最大连接数
     */
    private Integer maxTotal = 50;
    /**
     * 最大空闲连接数
     */
    private Integer maxIdle = 10;
    /**
     * 最小空闲连接数
     */
    private Integer minIdle = 5;


}
