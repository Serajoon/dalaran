package com.serajoon.dalaran.support.ftp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Fileupload implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     *
     * @mbggenerated
     */
    private String id;

    /**
     * 文件原始名称
     *
     * @mbggenerated
     */
    private String fileOldName;

    /**
     * 文件新名称
     *
     * @mbggenerated
     */
    private String fileNewName;

    /**
     * 所属项目上下文
     *
     * @mbggenerated
     */
    private String contextPath;

    /**
     * 内容类型
     *
     * @mbggenerated
     */
    private String contentType;

    /**
     * 文件类型
     *
     * @mbggenerated
     */
    private String fileType;

    /**
     * 文件位置
     *
     * @mbggenerated
     */
    private String location;

    /**
     * 1:ftp 2:local
     *
     * @mbggenerated
     */
    private Integer serverType;

    /**
     * 上传人id
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * 上传人ip
     *
     * @mbggenerated
     */
    private String userIp;

    /**
     * @mbggenerated
     */
    private String createTime;


}