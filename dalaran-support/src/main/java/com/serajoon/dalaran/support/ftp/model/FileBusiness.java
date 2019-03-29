package com.serajoon.dalaran.support.ftp.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FileBusiness implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     *
     */
    private String id;

    /**
     * 业务类型
     *
     */
    private String businessType;

    /**
     * 业务名称
     *
     */
    private String businessName;

    /**
     * 业务表主键
     *
     */
    private String businessId;

    /**
     * 附件表主键
     *
     */
    private String fileId;
}