package com.serajoon.dalaran.support.ftp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
/**
 *
 * @author  hanmeng
 * @since  2019/1/29 13:37
 */
@Builder
@Data
public class FtpDTO{

    /**
     * 业务主键ID
     */
    private String businessId;

    /**
     * 业务主键类型
     */
    private String businessType;

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 附件表Id list
     */
    private List<String> fileIdList;
}
