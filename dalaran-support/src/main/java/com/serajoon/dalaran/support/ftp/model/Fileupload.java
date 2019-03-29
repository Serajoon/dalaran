package com.serajoon.dalaran.support.ftp.model;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fileupload implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;

    /**
     * 文件原始名称
     */
    private String fileOldName;

    /**
     * 文件新名称
     */
    private String fileNewName;

    /**
     * 所属项目上下文
     */
    private String contextPath;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件位置
     */
    private String location;

    /**
     * 上传类型
     * 1:ftp 2:local
     */
    private Integer serverType;

    /**
     * 上传人id
     */
    private String userId;

    /**
     * 上传人ip
     */
    private String userIp;

    /**
     * 创建时间
     */
    private String createTime;


}